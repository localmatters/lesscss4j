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

public class ConstantExpression extends AbstractElement implements Expression {
    public static final String UNIT_COLOR = "color";

    private ConstantValue _value;

    public ConstantExpression() {
        this((ConstantValue)null);
    }

    public ConstantExpression(ConstantValue value) {
        _value = value;
    }

    public ConstantExpression(String value) {
        if (value.charAt(0) == '#' || ConstantColor.isColorFunction(value)) {
            _value = new ConstantColor(value);
        }
        else {
            _value = new ConstantNumber(value);
        }
    }

    public ConstantValue getValue() {
        return _value;
    }

    public void setValue(ConstantValue value) {
        _value = value;
    }

    public Expression evaluate(EvaluationContext context) {
        // Evaluation of a constant is itself
        return this;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
