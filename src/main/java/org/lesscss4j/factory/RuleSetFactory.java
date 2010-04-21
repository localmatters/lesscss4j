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
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.Selector;

import static org.lesscss4j.parser.LessCssLexer.*;

public class RuleSetFactory extends AbstractObjectFactory<RuleSet> {
    private ObjectFactory<Declaration> _declarationFactory;
    private ObjectFactory<Selector> _selectorFactory;

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
        for (int idx = 0, numChildren = ruleSetNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = ruleSetNode.getChild(idx);
            switch (child.getType()) {
                case SELECTOR:
                    Selector selector = getSelectorFactory().create(child);
                    if (selector != null) {
                        ruleSet.addSelector(selector);
                    }
                    break;

                case DECLARATION:
                    Declaration declaration = getDeclarationFactory().create(child);
                    if (declaration != null) {
                        ruleSet.addDeclaration(declaration);
                    }
                    break;

                default:
                    handleUnexpectedChild("Unexpected ruleset child:", child);
                    break;
            }
        }

        // Ignore empty rule sets
        if (ruleSet.getDeclarations() == null || ruleSet.getDeclarations().size() == 0) {
            ruleSet = null;
        }

        return ruleSet;
    }
}
