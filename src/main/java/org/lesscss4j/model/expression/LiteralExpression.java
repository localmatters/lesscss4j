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
    private String _value;

    public LiteralExpression() {
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

    public Expression evaluate(EvaluationContext context) {
        return this;
    }

    @Override
    public String toString() {
        return getValue();
    }
}