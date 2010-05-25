/**
 * File: AbstractDeclarationContainerTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:31:55 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.transform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.lesscss4j.error.ErrorUtils;
import org.lesscss4j.error.LessCssException;
import org.lesscss4j.error.MixinArgumentMismatchException;
import org.lesscss4j.error.UndefinedMixinReference;
import org.lesscss4j.model.BodyElement;
import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.DeclarationContainer;
import org.lesscss4j.model.DeclarationElement;
import org.lesscss4j.model.MixinReference;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.Selector;
import org.lesscss4j.model.expression.Expression;

public abstract class AbstractDeclarationContainerTransformer<T extends DeclarationContainer>
    extends AbstractTransformer<T> {

    private Transformer<RuleSet> _ruleSetTransformer;
    private Transformer<Declaration> _declarationTransformer;

    public Transformer<RuleSet> getRuleSetTransformer() {
        return _ruleSetTransformer;
    }

    public void setRuleSetTransformer(Transformer<RuleSet> ruleSetTransformer) {
        _ruleSetTransformer = ruleSetTransformer;
    }

    public Transformer<Declaration> getDeclarationTransformer() {
        return _declarationTransformer;
    }

    public void setDeclarationTransformer(Transformer<Declaration> declarationTransformer) {
        _declarationTransformer = declarationTransformer;
    }

    protected void transformDeclarations(T container, T transformed, EvaluationContext context) {

        if (!container.isMixinReferenceUsed()) {
            EvaluationContext declContext = new EvaluationContext();
            declContext.setParentContext(context);
            declContext.setVariableContainer(container);
            declContext.setRuleSetContainer(container);

            for (DeclarationElement declaration : container.getDeclarations()) {
                if (declaration instanceof Declaration) {
                    transformed.addDeclarations(
                                getDeclarationTransformer().transform((Declaration) declaration, declContext));
                }
                else {
                    // todo: error
                }
            }
        }
        else {
            transformDeclarationsWithMixins(container, transformed, context);
        }
    }

    private void transformDeclarationsWithMixins(DeclarationContainer container,
                                                 DeclarationContainer transformed,
                                                 EvaluationContext context) {

        EvaluationContext declContext = new EvaluationContext();
        declContext.setParentContext(context);
        declContext.setVariableContainer(transformed);
        declContext.setRuleSetContainer(transformed);


        // Mixins might define additional variables referenced by declaration values.
        // So we need to process all of the mixins before we transform the declarations.
        List<Declaration> declarationList = new ArrayList<Declaration>(container.getDeclarations().size());
        for (DeclarationElement declaration : container.getDeclarations()) {
            if (declaration instanceof Declaration) {
                declarationList.add((Declaration) declaration);
            }
            else if (declaration instanceof MixinReference) {
                MixinReference mixin = (MixinReference) declaration;
                try {
                    Selector selector = mixin.getSelector();
                    List<RuleSet> ruleSetList = context.getRuleSet(selector);
                    if (ruleSetList != null) {
                        for (RuleSet ruleSet : ruleSetList) {
                            ruleSet = ruleSet.clone();

                            updateMixinArguments(ruleSet, mixin);

                            List<RuleSet> mixinRuleSets = getRuleSetTransformer().transform(ruleSet, declContext);

                            ruleSet = mixinRuleSets.get(0);


                            for (Iterator<String> iter = ruleSet.getVariableNames(); iter.hasNext();) {
                                String varName = iter.next();
                                Expression expression = ruleSet.getVariable(varName);
                                transformed.setVariable(varName, expression);

                            }

                            for (DeclarationElement element : ruleSet.getDeclarations()) {
                                if (element instanceof Declaration) {
                                    declarationList.add((Declaration) element);
                                }
                            }

                            for (BodyElement bodyElement : ruleSet.getBodyElements()) {
                                // todo: check for collisions with selector names
                                transformed.addBodyElement(bodyElement);
                            }
                        }
                    }
                    else {
                        throw new UndefinedMixinReference(mixin);
                    }
                }
                catch (LessCssException ex) {
                    ErrorUtils.handleError(context.getErrorHandler(), mixin, ex);
                }
            }
        }

        for (Declaration declaration : declarationList) {
            transformed.addDeclarations(getDeclarationTransformer().transform(declaration, declContext));
        }
    }

    /**
     * Updates the variables in the rule set with the values from the argument list.  The values are either the default
     * values defined in the mixin or the values specified in the mixin call argument list.
     *
     * @param ruleSet The rule set to update
     * @param mixin   The mixin reference containing the call arguments
     */
    protected void updateMixinArguments(RuleSet ruleSet, MixinReference mixin) {
        if (mixin.getArguments().size() > ruleSet.getArguments().size()) {
            throw new MixinArgumentMismatchException(mixin, ruleSet);
        }

        if (ruleSet.getArguments().size() > 0) {

            // If there are no call arguments, we're done.  The RuleSet had it's variable
            // map updated with the default argument values when it was constructed.
            if (mixin.getArguments().size() > 0) {
                Iterator<Expression> callIter = mixin.getArguments().iterator();
                Iterator<Map.Entry<String, Expression>> argIter = ruleSet.getArguments().entrySet().iterator();

                while (callIter.hasNext()) {
                    Expression mixinValue = callIter.next();
                    Map.Entry<String, Expression> entry = argIter.next();

                    ruleSet.setVariable(entry.getKey(), mixinValue);
                }
            }

            ruleSet.getArguments().clear();
        }
    }

    protected void transformRuleSets(List<T> transformed, EvaluationContext context) {
        T container = transformed.get(0);
        if (container.getRuleSetCount() == 0) {
            return;
        }

        EvaluationContext ruleSetContext = new EvaluationContext();
        ruleSetContext.setParentContext(context);
        ruleSetContext.setVariableContainer(container);
        ruleSetContext.setRuleSetContainer(container);

        // First transform all the child rule sets.
        List<BodyElement> elements = container.getBodyElements();
        for (BodyElement element : elements) {
            if (element instanceof RuleSet) {
                RuleSet childRuleSet = (RuleSet) element;
                List<RuleSet> transformedList = getRuleSetTransformer().transform(childRuleSet, ruleSetContext);
                for (RuleSet transformedChild : transformedList) {
                    if (container instanceof RuleSet) {
                        updateChildSelectors((RuleSet) container, transformedChild);
                    }
                    transformed.add((T) transformedChild);
                }
            }
        }
    }

    protected void updateChildSelectors(RuleSet parent, RuleSet child) {
        List<Selector> selectorList = new ArrayList<Selector>();
        for (Selector parentSelector : parent.getSelectors()) {
            for (ListIterator<Selector> iter = child.getSelectors().listIterator(); iter.hasNext();) {
                Selector childSelector = iter.next();
                selectorList.add(new Selector(parentSelector, childSelector));
            }
        }

        child.setSelectors(selectorList);
    }

    public void doTransform(T container, List<T> transformed, EvaluationContext context) {
        evaluateVariables(container, transformed.get(0), context);
        transformDeclarations(container, transformed.get(0), context);
        transformRuleSets(transformed, context);
    }
}
