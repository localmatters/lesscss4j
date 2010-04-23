/**
 * File: VariableReferenceExpression.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 9:07:26 PM
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

import org.lesscss4j.exception.UndefinedVariableException;

public class VariableReferenceExpression implements Expression {
    private String _variableName;

    public VariableReferenceExpression() {
        this(null);
    }

    public VariableReferenceExpression(String variableName) {
        _variableName = variableName;
    }

    public String getVariableName() {
        return _variableName;
    }

    public void setVariableName(String variableName) {
        _variableName = variableName;
    }

    public Expression evaluate(EvaluationContext context) {
        Expression value = context.getVariableExpression(getVariableName());
        if (value == null) {
            throw new UndefinedVariableException(getVariableName()); // todo: pass 'this' instead?
        }
        return value.evaluate(context);
    }
}
