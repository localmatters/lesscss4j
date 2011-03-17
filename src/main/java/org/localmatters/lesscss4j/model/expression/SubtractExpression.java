/**
 * File: MultiplyExpression.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 8:48:08 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.localmatters.lesscss4j.model.expression;

public class SubtractExpression extends CompoundExpression {
    public SubtractExpression() {
    }

    public SubtractExpression(SubtractExpression copy) {
        super(copy);
    }

    public SubtractExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public ConstantValue evaluate(ConstantValue left, ConstantValue right) {
        return left.subtract(right);
    }

    @Override
    public SubtractExpression clone() {
        return new SubtractExpression(this);
    }
}