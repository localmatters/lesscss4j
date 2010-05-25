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
package org.lesscss4j.model.expression;

public class MultiplyExpression extends CompoundExpression {
    public MultiplyExpression() {
    }

    public MultiplyExpression(MultiplyExpression copy) {
        super(copy);
    }

    public MultiplyExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public ConstantValue evaluate(ConstantValue left, ConstantValue right) {
        return left.multiply(right);
    }

    @Override
    public MultiplyExpression clone() {
        return new MultiplyExpression(this);
    }
}
