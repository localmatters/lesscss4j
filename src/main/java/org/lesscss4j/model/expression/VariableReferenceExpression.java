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

import org.lesscss4j.error.UndefinedVariableException;
import org.lesscss4j.model.AbstractElement;
import org.lesscss4j.transform.EvaluationContext;

public class VariableReferenceExpression extends AbstractElement implements Expression {
    private String _variableName;

    public VariableReferenceExpression() {
        this((String)null);
    }

    public VariableReferenceExpression(VariableReferenceExpression copy) {
        super(copy);
        _variableName = copy._variableName;
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
        Expression value = context.getVariable(getVariableName());
        if (value == null) {
            throw new UndefinedVariableException(this);
        }
        return value.evaluate(context);
    }

    public VariableReferenceExpression clone() {
        return new VariableReferenceExpression(this);
    }
}
