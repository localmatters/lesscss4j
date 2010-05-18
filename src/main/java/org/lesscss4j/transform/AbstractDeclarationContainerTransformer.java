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

import org.lesscss4j.exception.UndefinedMixinReference;
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

    protected void transformDeclarations(DeclarationContainer container, EvaluationContext context) {
        EvaluationContext declContext = new EvaluationContext();
        declContext.setParentContext(context);
        declContext.setVariableContainer(container);
        declContext.setRuleSetContainer(container);

        if (!container.isMixinReferenceUsed()) {
            // No need to create the flattened declaration map.  Just transform the declarations in place
            for (DeclarationElement declaration : container.getDeclarations()) {
                if (declaration instanceof Declaration) {
                    getDeclarationTransformer().transform((Declaration) declaration, declContext);
                }
            }
        }
        else {

            // Insert any mixin references into the declaration list.  Then evaluate them all at the end.
            List<Declaration> flattenedDeclarations = new ArrayList<Declaration>(container.getDeclarations().size());
            for (DeclarationElement declaration : container.getDeclarations()) {
                if (declaration instanceof Declaration) {
                    flattenedDeclarations.add((Declaration) declaration);
                }
                else if (declaration instanceof MixinReference) {
                    MixinReference mixin = (MixinReference) declaration;
                    Selector selector = mixin.getSelector();
                    List<RuleSet> ruleSetList = context.getRuleSet(selector);
                    if (ruleSetList != null) {
                        for (RuleSet ruleSet : ruleSetList) {
                            if (ruleSet.isMixinReferenceUsed()) {
                                throw new UndefinedMixinReference("Mixins must be defined before their use", mixin);
                            }

                            ruleSet = evaluateMixinArguments(ruleSet, mixin, declContext);

                            for (Iterator<String> iter = ruleSet.getVariableNames(); iter.hasNext(); ) {
                                String varName = iter.next();
                                Expression expression = ruleSet.getVariable(varName);
                                container.setVariable(varName, expression);

                            }
                            for (DeclarationElement element : ruleSet.getDeclarations()) {
                                if (element instanceof Declaration) {
                                    flattenedDeclarations.add((Declaration) element);
                                }
                            }
                        }
                    }
                    else {
                        throw new UndefinedMixinReference("Mixin not found", mixin);
                    }
                }
            }

            container.clearDeclarations();
            for (Declaration declaration : flattenedDeclarations) {
                getDeclarationTransformer().transform(declaration, declContext);
                container.addDeclaration(declaration);
            }
        }
    }

    protected RuleSet evaluateMixinArguments(RuleSet ruleSet, MixinReference mixin, EvaluationContext declContext) {
        EvaluationContext context = declContext.getParentContext();
        if (mixin.getArguments().size() > ruleSet.getArguments().size()) {
            // todo: exception...more arguments specified than available?
        }

        // If the rule set takes arguments, we need to evaluate it on the fly.  So make a copy and run it
        // through the process as if it didn't have any arguments.
        if (ruleSet.getArguments().size() > 0) {
            ruleSet = ruleSet.clone();

            if (mixin.getArguments().size() > 0) {
                Iterator<Expression> callIter = mixin.getArguments().iterator();
                Iterator<Map.Entry<String, Expression>> argIter =
                    ruleSet.getArguments().entrySet().iterator();

                while (callIter.hasNext()) {
                    Expression mixinValue = callIter.next();
                    Map.Entry<String, Expression> entry = argIter.next();

                    ruleSet.setVariable(entry.getKey(), mixinValue);
                }
            }
            
            ruleSet.getArguments().clear();

            evaluateVariables(ruleSet, declContext);
            transformDeclarations(ruleSet, context); // todo: not sure if this is the right context
        }
        return ruleSet;
    }

    protected void transformRuleSets(DeclarationContainer container, EvaluationContext context) {
        if (container.getRuleSetCount() == 0) {
            return;
        }

        EvaluationContext ruleSetContext = new EvaluationContext();
        ruleSetContext.setParentContext(context);
        ruleSetContext.setVariableContainer(container);
        ruleSetContext.setRuleSetContainer(container);

        // First transform all the child rule sets.
        List<BodyElement> elements = container.getBodyElements();
        for (int idx = 0; idx < elements.size(); idx++) {
            BodyElement element = elements.get(idx);
            if (element instanceof RuleSet) {
                RuleSet childRuleSet = (RuleSet) element;
                ruleSetContext.setRuleSetIndex(idx);
                getRuleSetTransformer().transform(childRuleSet, ruleSetContext);
                if (ruleSetContext.getRuleSetIndex() > idx) {
                    idx = ruleSetContext.getRuleSetIndex();
                }
            }
        }

        // Now take all the child rule sets, update their selectors to include the current node
        for (BodyElement element : container.getBodyElements()) {
            if (element instanceof RuleSet) {
                RuleSet childRuleSet = (RuleSet) element;
                if (container instanceof RuleSet) {
                    RuleSet ruleSet = (RuleSet) container;

                    List<Selector> selectorList = new ArrayList<Selector>();
                    for (Selector selector : ruleSet.getSelectors()) {
                        for (ListIterator<Selector> iterator =
                            childRuleSet.getSelectors().listIterator(); iterator.hasNext();) {
                            Selector childSelector = iterator.next();
                            Selector mergedSelector = new Selector(selector, childSelector);
                            selectorList.add(mergedSelector);
                        }
                    }

                    childRuleSet.setSelectors(selectorList);
                }


                context.setRuleSetIndex(context.getRuleSetIndex() + 1);
                context.getRuleSetContainer().addRuleSet(childRuleSet, context.getRuleSetIndex());
            }
        }

        container.clearBodyElements();
    }

    public void transform(DeclarationContainer container, EvaluationContext context) {
        if (container instanceof RuleSet && ((RuleSet)container).getArguments().size() > 0) {
            return;
        }

        evaluateVariables(container, context);
        transformDeclarations(container, context);
        transformRuleSets(container, context);
    }
}
