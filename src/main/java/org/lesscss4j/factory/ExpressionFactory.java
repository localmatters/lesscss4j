/**
 * File: ExpressionFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 4:14:00 PM
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
import org.lesscss4j.model.expression.AddExpression;
import org.lesscss4j.model.expression.ConstantExpression;
import org.lesscss4j.model.expression.DivideExpression;
import org.lesscss4j.model.expression.Expression;
import org.lesscss4j.model.expression.FunctionExpression;
import org.lesscss4j.model.expression.LiteralExpression;
import org.lesscss4j.model.expression.MultiplyExpression;
import org.lesscss4j.model.expression.SubtractExpression;
import org.lesscss4j.model.expression.VariableReferenceExpression;

import static org.lesscss4j.parser.LessCssLexer.*;

public class ExpressionFactory extends AbstractObjectFactory<Expression> {
    public Expression create(Tree expression) {

        switch (expression.getType()) {
            case FUNCTION:
                return createFunction(expression);

            case EXPR: {
                return createExpression(expression.getChild(0));
            }

            default:
                handleUnexpectedChild("Unexpected expression type", expression);
                return null; // shouldn't get here
        }
    }

    protected Expression createExpression(Tree expression) {
        switch (expression.getType()) {
            case CONSTANT:
                return new ConstantExpression(expression.getChild(0).getText());

            case LITERAL:
                return new LiteralExpression(expression.getChild(0).getText());

            case STAR:
                return new MultiplyExpression(createExpression(expression.getChild(0)),
                                              createExpression(expression.getChild(1)));

            case SOLIDUS:
                return new DivideExpression(createExpression(expression.getChild(0)),
                                            createExpression(expression.getChild(1)));

            case PLUS:
                return new AddExpression(createExpression(expression.getChild(0)),
                                         createExpression(expression.getChild(1)));

            case MINUS:
                return new SubtractExpression(createExpression(expression.getChild(0)),
                                              createExpression(expression.getChild(1)));

            case VAR:
                return new VariableReferenceExpression(expression.getChild(0).getText());

            default:
                handleUnexpectedChild("Unexpected expression type", expression);
                return null; // shouldn't get here
        }
    }

    protected Expression createFunction(Tree function) {
        Tree nameNode = function.getChild(0);
        FunctionExpression func = new FunctionExpression(nameNode.getText());
        switch (nameNode.getType()) {
            case EXPRESSION:
                break;

            case ALPHA:
                for (int idx = 1, numChildren = function.getChildCount(); idx < numChildren; idx++) {
                    Tree child = function.getChild(idx);
                    String prop = child.getChild(0).getText();
                    Expression expr = create(child.getChild(1));
                    func.addArgument(new LiteralExpression(prop));
                    func.addArgument(new LiteralExpression(child.getText()));
                    func.addArgument(expr);
                }
                break;

            case IDENT:
                break;
        }
        return func;
    }
}
