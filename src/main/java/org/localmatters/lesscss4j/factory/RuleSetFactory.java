/**
 * File: RuleSetFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 10:56:47 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.localmatters.lesscss4j.factory;

import org.antlr.runtime.tree.Tree;
import org.localmatters.lesscss4j.error.ErrorHandler;
import org.localmatters.lesscss4j.model.Declaration;
import org.localmatters.lesscss4j.model.MixinReference;
import org.localmatters.lesscss4j.model.RuleSet;
import org.localmatters.lesscss4j.model.Selector;
import org.localmatters.lesscss4j.model.expression.Expression;

import static org.localmatters.lesscss4j.parser.antlr.LessCssLexer.*;

public class RuleSetFactory extends AbstractObjectFactory<RuleSet> {
    private ObjectFactory<Declaration> _declarationFactory;
    private ObjectFactory<Selector> _selectorFactory;
    private ObjectFactory<Expression> _expressionFactory;

    public ObjectFactory<Expression> getExpressionFactory() {
        return _expressionFactory;
    }

    public void setExpressionFactory(ObjectFactory<Expression> expressionFactory) {
        _expressionFactory = expressionFactory;
    }

    public ObjectFactory<Declaration> getDeclarationFactory() {
        return _declarationFactory;
    }

    public void setDeclarationFactory(ObjectFactory<Declaration> declarationFactory) {
        _declarationFactory = declarationFactory;
    }

    public ObjectFactory<Selector> getSelectorFactory() {
        return _selectorFactory;
    }

    public void setSelectorFactory(ObjectFactory<Selector> selectorFactory) {
        _selectorFactory = selectorFactory;
    }

    public RuleSet create(Tree ruleSetNode, ErrorHandler errorHandler) {
        RuleSet ruleSet = new RuleSet();
        ruleSet.setLine(ruleSetNode.getLine());
        ruleSet.setChar(ruleSetNode.getCharPositionInLine());

        for (int idx = 0, numChildren = ruleSetNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = ruleSetNode.getChild(idx);
            switch (child.getType()) {
                case SELECTOR:
                    Selector selector = getSelectorFactory().create(child, errorHandler);
                    if (selector != null) {
                        ruleSet.addSelector(selector);
                    }
                    break;

                case MIXIN_ARG:
                case VAR:
                    Expression expr = getExpressionFactory().create(child.getChild(1), null);
                    if (expr != null) {
                        String varName = child.getChild(0).getText();
                        if (ruleSet.getVariable(varName) != null) {
                            // todo: error -- duplicate error
                        }
                        ruleSet.setVariable(varName, expr);

                        if (child.getType() == MIXIN_ARG) {
                            ruleSet.addArgument(varName, expr);
                        }
                    }
                    break;

                case DECLARATION:
                    Declaration declaration = getDeclarationFactory().create(child, errorHandler);
                    if (declaration != null) {
                        ruleSet.addDeclaration(declaration);
                    }
                    break;

                case MIXIN_REF:
                    MixinReference ref = createMixinReferences(child, errorHandler);
                    if (ref != null) {
                        ruleSet.addDeclaration(ref);
                    }
                    break;

                case RULESET:
                    RuleSet childRuleSet = create(child, errorHandler);
                    if (childRuleSet != null) {
                        ruleSet.addRuleSet(childRuleSet, -1);
                    }
                    break;

                default:
                    handleUnexpectedChild("Unexpected ruleset child:", child);
                    break;
            }
        }

        return ruleSet;
    }

    protected MixinReference createMixinReferences(Tree mixinNode, ErrorHandler errorHandler) {
        // todo: put this in it's own factory?

        MixinReference ref = new MixinReference();
        ref.setLine(mixinNode.getLine());
        ref.setChar(mixinNode.getCharPositionInLine());

        for (int idx = 0, numChildren = mixinNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = mixinNode.getChild(idx);
            switch (child.getType()) {
                case SELECTOR:
                    Selector selector = getSelectorFactory().create(child, errorHandler);
                    ref.setSelector(selector);
                    break;

                case MIXIN_ARG:
                    Expression arg = getExpressionFactory().create(child.getChild(0), errorHandler);
                    if (arg != null) {
                        ref.addArgument(arg);
                    }
                    break;

                default:
                    handleUnexpectedChild("Unexpected mixin reference child", child);
            }
        }
        return ref;
    }
}
