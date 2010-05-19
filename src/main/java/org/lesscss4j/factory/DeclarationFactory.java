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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.lesscss4j.error.ErrorHandler;
import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.expression.Expression;
import org.lesscss4j.model.expression.FunctionExpression;
import org.lesscss4j.model.expression.LiteralExpression;
import org.lesscss4j.parser.antlr.LessCssLexer;
import org.lesscss4j.parser.antlr.LessCssParser;

import static org.lesscss4j.parser.antlr.LessCssLexer.*;

public class DeclarationFactory extends AbstractObjectFactory<Declaration> {
    private ObjectFactory<Expression> _expressionFactory;

    public ObjectFactory<Expression> getExpressionFactory() {
        return _expressionFactory;
    }

    public void setExpressionFactory(ObjectFactory<Expression> expressionFactory) {
        _expressionFactory = expressionFactory;
    }

    public Declaration create(Tree declarationNode, ErrorHandler errorHandler) {
        Declaration declaration = new Declaration();
        declaration.setLine(declarationNode.getLine());
        declaration.setChar(declarationNode.getCharPositionInLine());

        for (int idx = 0, numChildren = declarationNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = declarationNode.getChild(idx);
            switch (child.getType()) {
                case IDENT:
                case FONT:
                    String propName = child.getText();
                    Tree propChild = child.getChild(0);
                    if (propChild != null) {
                        propName = propChild.getText() + propName;
                    }
                    declaration.setProperty(propName);
                    break;

                case PROP_VALUE:
                    List<Object> values = createPropValues(child, errorHandler);
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

    protected List<Object> createPropValues(Tree valueNode, ErrorHandler errorHandler) {
        int numChildren = valueNode.getChildCount();
        List<Object> values = new ArrayList<Object>(numChildren);
        for (int idx = 0; idx < numChildren; idx++) {
            Tree child = valueNode.getChild(idx);
            switch (child.getType()) {
                case LITERAL:
                case EXPR:
                case EXPRESSION:
                case FUNCTION:
                    Expression expression = getExpressionFactory().create(child, errorHandler);
                    if (expression != null) {
                        Expression ieFilter = parseIE8AlphaFilter(expression, errorHandler);
                        if (ieFilter != null) {
                            expression = ieFilter;
                        }
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

    private Pattern _ieAlphaPattern =
        Pattern.compile("(?i)['\"]progid:DXImageTransform\\.Microsoft\\.(Alpha\\(.*\\))['\"]");

    /**
     * We can't handle the IE8 way of processing Alpha in the Lexer...it just looks like a literal string.  This method
     * takes the LiteralExpression and attempts to parse it as a declaration property value so that variables and
     * expressions can be used in the opacity value.
     *
     * @param value The Literal expression to parse
     * @param handler
     * @return The parsed Expression.  Null if it isn't an Alpha expression or it cannot be parsed.
     */
    protected Expression parseIE8AlphaFilter(Expression value, ErrorHandler errorHandler) {
        if (value instanceof LiteralExpression) {
            String text = ((LiteralExpression) value).getValue();

            // Short circuit test to avoid doing the regex match if we don't have to
            if ((text.charAt(0) == '"' || text.charAt(0) == '\'') &&
                text.length() > "'progid:DXImageTransform.Microsoft.Alpha()'".length() &&
                (text.charAt(text.length() - 1) == '"' || text.charAt(text.length() - 1) == '\'')) {

                Matcher matcher = _ieAlphaPattern.matcher(text);
                if (matcher.matches()) {
                    LessCssLexer lexer = new LessCssLexer(new ANTLRStringStream(matcher.group(1)));
                    LessCssParser parser = new LessCssParser(new CommonTokenStream(lexer));
                    try {
                        LessCssParser.propertyValue_return result = parser.propertyValue();
                        List<Object> propValues = createPropValues((Tree) result.getTree(), errorHandler);
                        FunctionExpression alphaFunction = (FunctionExpression) propValues.get(0);
                        alphaFunction.setName("progid:DXImageTransform.Microsoft.Alpha");
                        alphaFunction.setQuoted(true);
                        return alphaFunction;
                    }
                    catch (RecognitionException e) {
                        // todo: send something to the error handler
                        // Can't do anything with it.  Just leave it alone
                    }
                }
            }
        }
        return null;
    }
}
