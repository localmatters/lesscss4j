/**
 * File: PageFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:40:25 AM
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
import org.lesscss4j.model.Page;
import org.lesscss4j.model.expression.Expression;

import static org.lesscss4j.parser.LessCssLexer.*;

public class PageFactory extends AbstractObjectFactory<Page> {
    private ObjectFactory<Declaration> _declarationFactory;
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

    public Page create(Tree pageNode) {
        Page page = new Page();
        for (int idx = 0, numChildren = pageNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = pageNode.getChild(idx);
            switch (child.getType()) {
                case DECLARATION:
                    Declaration declaration = getDeclarationFactory().create(child);
                    if (declaration != null) {
                        page.addDeclaration(declaration);
                    }
                    break;

                case VAR:
                    Expression expr = getExpressionFactory().create(child.getChild(1));
                    if (expr != null) {
                        page.setVariable(child.getChild(0).getText(), expr);
                    }
                    break;


                default:
                    handleUnexpectedChild("Unexpected page child:", child);
                    break;
            }
        }

        return page.getDeclarations() != null && page.getDeclarations().size() > 0 ? page : null;
    }
}
