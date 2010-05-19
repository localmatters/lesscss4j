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
package org.lesscss4j.error;

import org.lesscss4j.model.expression.ConstantValue;

public class UnitMismatchException extends LessCssException {
    private ConstantValue _left;
    private ConstantValue _right;

    public UnitMismatchException(ConstantValue left, ConstantValue right) {
        super("Unit mismatch: " + left.toString() + " " + right.toString());
        _left = left;
        _right = right;
    }

    public UnitMismatchException(String message, ConstantValue left, ConstantValue right) {
        super("Unit mismatch: " + message + ": " + left.toString() + " " + right.toString());
        _left = left;
        _right = right;
    }

    public UnitMismatchException(String message, ConstantValue left, ConstantValue right, Throwable cause) {
        super("Unit mismatch:" + message + ": " + left.toString() + " " + right.toString(), cause);
        _left = left;
        _right = right;
    }

    public UnitMismatchException(ConstantValue left, ConstantValue right, Throwable cause) {
        super("Unit mismatch:" + left.toString() + " " + right.toString(),cause);
        _left = left;
        _right = right;
    }

    public ConstantValue getLeft() {
        return _left;
    }

    public ConstantValue getRight() {
        return _right;
    }
}
