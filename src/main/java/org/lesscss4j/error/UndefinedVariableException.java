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

public class UndefinedVariableException extends LessCssException {
    public UndefinedVariableException(String variableName) {
        super(variableName);
    }

    public UndefinedVariableException(String variableName, String message) {
        super(variableName + ':' + message);
    }

    public UndefinedVariableException(String variableName, String message, Throwable cause) {
        super(variableName + ':' + message, cause);
    }

    public UndefinedVariableException(String variableName, Throwable cause) {
        super(variableName, cause);
    }
}
