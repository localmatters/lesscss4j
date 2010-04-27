/**
 * File: DeclarationFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 10:29:11 AM
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

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.lesscss4j.model.expression.Expression;
import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.expression.LiteralExpression;

import static org.lesscss4j.parser.antlr.LessCssLexer.*;

public class DeclarationFactory extends AbstractObjectFactory<Declaration> {
    private ObjectFactory<Expression> _expressionFactory;

    public ObjectFactory<Expression> getExpressionFactory() {
        return _expressionFactory;
    }

    public void setExpressionFactory(ObjectFactory<Expression> expressionFactory) {
        _expressionFactory = expressionFactory;
    }

    public Declaration create(Tree declarationNode) {
        Declaration declaration = new Declaration();

        for (int idx = 0, numChildren = declarationNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = declarationNode.getChild(idx);
            switch (child.getType()) {
                case IDENT:
                case FONT:
                    declaration.setProperty(child.getText());
                    if (child.getChild(0) != null && child.getChild(0).getType() == STAR) {
                        declaration.setStar(true);
                    }
                    break;

                case PROP_VALUE:
                    List<Object> values = createPropValues(child);
                    if (values != null) {
                        declaration.setValues(values);
                    }
                    break;

                case IMPORTANT_SYM:
                    declaration.setImportant(true);
                    break;

                default:
                    handleUnexpectedChild("Unexpected declaration child:", child);
                    break;
            }
        }

        return declaration.getValues() == null ? null : declaration;
    }

    protected List<Object> createPropValues(Tree valueNode) {
        int numChildren = valueNode.getChildCount();
        List<Object> values = new ArrayList<Object>(numChildren);
        for (int idx = 0; idx < numChildren; idx++) {
            Tree child = valueNode.getChild(idx);
            switch (child.getType()) {
                case LITERAL:
                    values.add(new LiteralExpression(concatChildNodeText(child)));
                    break;

                case EXPR:
                case FUNCTION:
                    Expression expression = getExpressionFactory().create(child);
                    if (expression != null) {
                        values.add(expression);
                    }
                    break;

                case COMMA:
                case NUMBER:
                case SOLIDUS:
                case IDENT:
                case STRING:
                    // These tokens just spit out as is
                    values.add(child.getText());
                    break;

                case WS:
                    values.add(" ");
                    break;

                default:
                    handleUnexpectedChild("Unexpected declaration value child:", child);
                    break;

            }
        }
        return values.size() > 0 ? values : null;
    }
}
