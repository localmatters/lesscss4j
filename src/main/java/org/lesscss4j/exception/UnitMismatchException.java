/**
 * File: UnitMismatchException.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 8:39:13 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.exception;

import org.lesscss4j.model.expression.ConstantValue;

public class UnitMismatchException extends ExpressionException {
    public UnitMismatchException(ConstantValue left, ConstantValue right) {
    }

    public UnitMismatchException(String message, ConstantValue left, ConstantValue right) {
        super(message);
    }

    public UnitMismatchException(String message, ConstantValue left, ConstantValue right, Throwable cause) {
        super(message, cause);
    }

    public UnitMismatchException(ConstantValue left, ConstantValue right, Throwable cause) {
        super(cause);
    }
}
