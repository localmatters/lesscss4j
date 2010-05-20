/**
 * File: DivideByZeroException.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 20, 2010
 * Creation Time: 3:51:14 PM
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

public class DivideByZeroException extends ExpressionException {
    public DivideByZeroException() {
        super("Division by zero.");
    }

    public DivideByZeroException(String message) {
        super("Division by zero. " + message);
    }

    public DivideByZeroException(String message, Throwable cause) {
        super("Division by zero. " + message, cause);
    }

    public DivideByZeroException(Throwable cause) {
        super("Division by zero.", cause);
    }
}
