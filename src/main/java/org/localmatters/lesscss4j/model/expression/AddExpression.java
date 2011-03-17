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

public class AddExpression extends CompoundExpression {
    public AddExpression() {
    }

    public AddExpression(Expression left, Expression right) {
        super(left, right);
    }

    public AddExpression(AddExpression copy) {
        super(copy);
    }

    @Override
    public ConstantValue evaluate(ConstantValue left, ConstantValue right) {
        return left.add(right);
    }

    @Override
    public AddExpression clone() {
        return new AddExpression(this);
    }
}