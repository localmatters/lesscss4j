/**
 * File: UndefinedVariableException.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 11:54:58 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.error;

import org.lesscss4j.model.expression.VariableReferenceExpression;

public class UndefinedVariableException extends LessCssException {
    public UndefinedVariableException(VariableReferenceExpression variable) {
        super("Undefined variable: @" + variable.getVariableName());
        setPosition(variable);
    }

    public UndefinedVariableException(VariableReferenceExpression variable, String message) {
        super("Undefined variable: @" + variable.getVariableName() + ": " + message);
        setPosition(variable);
    }

    public UndefinedVariableException(VariableReferenceExpression variable, String message, Throwable cause) {
        super("Undefined variable: @" + variable.getVariableName() + ": " + message, cause);
        setPosition(variable);
    }

    public UndefinedVariableException(VariableReferenceExpression variable, Throwable cause) {
        super("Undefined variable: @" + variable.getVariableName(), cause);
        setPosition(variable);
    }
}
