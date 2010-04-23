/**
 * File: CompoundExpression.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 4:08:45 PM
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

public class CompoundExpression implements Expression {
    private Expression _left;
    private Expression _right;

    public CompoundExpression() {
        this(null, null);
    }

    public CompoundExpression(Expression left, Expression right) {
        setLeft(left);
        setRight(right);
    }

    public Expression evaluate(EvaluationContext context) {
        Expression left = getLeft().evaluate(context);
        Expression right = getRight().evaluate(context);
        if (left instanceof ConstantExpression && right instanceof ConstantExpression) {
            ConstantValue result = evaluate(((ConstantExpression) left).getValue(),
                                     ((ConstantExpression) right).getValue());
            if (result != null) {
                return new ConstantExpression(result);
            }
        }

        return this;
    }

    public ConstantValue evaluate(ConstantValue left, ConstantValue right) {
        return null;
    }

    public Expression getLeft() {
        return _left;
    }

    public void setLeft(Expression left) {
        _left = left;
    }

    public Expression getRight() {
        return _right;
    }

    public void setRight(Expression right) {
        _right = right;
    }
}
