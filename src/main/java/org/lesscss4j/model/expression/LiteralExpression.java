/**
 * File: ConstantExpression.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 4:11:11 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.model.expression;

import org.lesscss4j.model.AbstractElement;
import org.lesscss4j.transform.EvaluationContext;

public class LiteralExpression extends AbstractElement implements Expression {
    /** Textual value of the literal expression */
    private String _value;

    /** The token type of this literal as defined in {@link org.lesscss4j.parser.antlr.LessCssLexer} */
    private int _type;

    public LiteralExpression() {
    }

    public LiteralExpression(LiteralExpression copy) {
        super(copy);
        _value = copy._value;
    }

    public LiteralExpression(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        _type = type;
    }

    public Expression evaluate(EvaluationContext context) {
        return this;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public LiteralExpression clone() {
        return new LiteralExpression(this);
    }
}