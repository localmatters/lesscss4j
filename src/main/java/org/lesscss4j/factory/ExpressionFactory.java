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
import org.lesscss4j.error.ErrorHandler;
import org.lesscss4j.model.AbstractElement;
import org.lesscss4j.model.expression.AddExpression;
import org.lesscss4j.model.expression.ConstantExpression;
import org.lesscss4j.model.expression.DivideExpression;
import org.lesscss4j.model.expression.Expression;
import org.lesscss4j.model.expression.FunctionExpression;
import org.lesscss4j.model.expression.ListExpression;
import org.lesscss4j.model.expression.LiteralExpression;
import org.lesscss4j.model.expression.MultiplyExpression;
import org.lesscss4j.model.expression.SubtractExpression;
import org.lesscss4j.model.expression.VariableReferenceExpression;

import static org.lesscss4j.parser.antlr.LessCssLexer.*;

public class ExpressionFactory extends AbstractObjectFactory<Expression> {
    public Expression create(Tree expression, ErrorHandler errorHandler) {

        switch (expression.getType()) {
            case FUNCTION:
                return createFunction(expression, errorHandler);

            case EXPR:
                if (expression.getChildCount() > 1) {
                    return createListExpression(expression, errorHandler);
                }
                else {
                    return createExpression(expression.getChild(0), errorHandler);
                }

            case LITERAL:
                return createLiteral(expression, errorHandler);

            default:
                handleUnexpectedChild("Unexpected expression type", expression);
                return null; // shouldn't get here
        }
    }

    protected LiteralExpression createLiteral(Tree expression, ErrorHandler errorHandler) {
        return createLiteral(concatChildNodeText(expression), expression, errorHandler);
    }

    protected LiteralExpression createLiteral(String text, Tree expression, ErrorHandler errorHandler) {
        LiteralExpression literal = new LiteralExpression(text);
        literal.setType(expression.getType());
        literal.setLine(expression.getLine());
        literal.setChar(expression.getCharPositionInLine());
        return literal;
    }

    protected Expression createListExpression(Tree expression, ErrorHandler errorHandler) {
        ListExpression listExpr = new ListExpression();
        for (int idx = 0, numChildren = expression.getChildCount(); idx < numChildren; idx++) {
            Tree child = expression.getChild(idx);
            switch (child.getType()) {
                case COMMA:
                case WS:
                    listExpr.addExpression(createLiteral(child.getText(), child, errorHandler));
                    break;

                default:
                    listExpr.addExpression(createExpression(child, errorHandler));
                    break;
            }
        }

        return listExpr;
    }

    protected Expression createExpression(Tree expression, ErrorHandler errorHandler) {
        Expression result;
        switch (expression.getType()) {
            case CONSTANT:
                result = new ConstantExpression(concatChildNodeText(expression));
                break;

            case LITERAL:
                result = createLiteral(expression, errorHandler);
                break;

            case STAR:
                result = new MultiplyExpression(createExpression(expression.getChild(0), errorHandler),
                                                createExpression(expression.getChild(1), errorHandler));
                break;

            case SOLIDUS:
                result = new DivideExpression(createExpression(expression.getChild(0), errorHandler),
                                              createExpression(expression.getChild(1), errorHandler));
                break;

            case PLUS:
                result = new AddExpression(createExpression(expression.getChild(0), errorHandler),
                                           createExpression(expression.getChild(1), errorHandler));
                break;

            case MINUS:
                result = new SubtractExpression(createExpression(expression.getChild(0), errorHandler),
                                                createExpression(expression.getChild(1), errorHandler));
                break;

            case VAR:
                result = new VariableReferenceExpression(expression.getChild(0).getText());
                break;

            case EXPR:
                result = createExpression(expression.getChild(0), errorHandler);
                break;

            default:
                handleUnexpectedChild("Unexpected expression type", expression);
                return null; // shouldn't get here
        }

        if (result instanceof AbstractElement) {
            ((AbstractElement) result).setLine(expression.getLine());
            ((AbstractElement) result).setChar(expression.getCharPositionInLine());
        }

        return result;
    }

    protected Expression createFunction(Tree function, ErrorHandler errorHandler) {
        Tree nameNode = function.getChild(0);
        FunctionExpression func = new FunctionExpression(nameNode.getText());
        switch (nameNode.getType()) {
            case ALPHA:
                for (int idx = 1, numChildren = function.getChildCount(); idx < numChildren; idx++) {
                    Tree child = function.getChild(idx);
                    Tree propNode = child.getChild(0);
                    String prop = propNode.getText();
                    Expression expr = create(child.getChild(1), null);
                    func.addArgument(createLiteral(prop, propNode, errorHandler));
                    func.addArgument(createLiteral(child.getText(), child, errorHandler));
                    func.addArgument(expr);
                }
                break;

            case EXPRESSION:
            case IDENT:
                for (int idx = 1, numChildren = function.getChildCount(); idx < numChildren; idx++) {
                    Tree child = function.getChild(idx);
                    switch (child.getType()) {
                        case FUNCTION:
                            func.addArgument(createFunction(child, errorHandler));
                            break;

                        case VAR:
                        case LITERAL:
                        case EXPR:
                            func.addArgument(createExpression(child, errorHandler));
                            break;

                        default:
                            func.addArgument(createLiteral(child.getText(), child, errorHandler));
                            break;

                    }
                }
                break;
        }

        func.setLine(function.getLine());
        func.setChar(function.getCharPositionInLine());
        return func;
    }
}
