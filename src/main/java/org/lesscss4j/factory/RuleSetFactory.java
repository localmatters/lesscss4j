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
package org.lesscss4j.factory;

import org.antlr.runtime.tree.Tree;
import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.MixinReference;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.Selector;
import org.lesscss4j.model.expression.Expression;

import static org.lesscss4j.parser.antlr.LessCssLexer.*;

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

    public RuleSet create(Tree ruleSetNode) {
        RuleSet ruleSet = new RuleSet();
        ruleSet.setLine(ruleSetNode.getLine());
        ruleSet.setChar(ruleSetNode.getCharPositionInLine());
        for (int idx = 0, numChildren = ruleSetNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = ruleSetNode.getChild(idx);
            switch (child.getType()) {
                case SELECTOR:
                    Selector selector = getSelectorFactory().create(child);
                    if (selector != null) {
                        ruleSet.addSelector(selector);
                    }
                    break;

                case VAR:
                    Expression expr = getExpressionFactory().create(child.getChild(1));
                    if (expr != null) {
                        ruleSet.setVariable(child.getChild(0).getText(), expr);
                    }
                    break;

                case DECLARATION:
                    Declaration declaration = getDeclarationFactory().create(child);
                    if (declaration != null) {
                        ruleSet.addDeclaration(declaration);
                    }
                    break;

                case MIXIN_REF:
                    createMixinReferences(child, ruleSet);
                    break;

                case RULESET:
                    RuleSet childRuleSet = create(child);
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

    protected void createMixinReferences(Tree mixinNode, RuleSet ruleSet) {
        for (int idx = 0, numChildren = mixinNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = mixinNode.getChild(idx);
            if (child.getType() == SELECTOR) {
                Selector selector = getSelectorFactory().create(child);
                MixinReference ref = new MixinReference();
                ref.setSelector(selector);
                ref.setLine(mixinNode.getLine());
                ref.setChar(mixinNode.getCharPositionInLine());

                ruleSet.addDeclaration(ref);
            }
            else {
                handleUnexpectedChild("Unexpected mixin reference child", child);
            }
        }
    }
}
