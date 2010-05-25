/**
 * File: ConstantValue.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 8:29:34 AM
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

public interface ConstantValue extends Cloneable {
    double getValue();

    ConstantValue add(ConstantValue right);

    ConstantValue subtract(ConstantValue right);

    ConstantValue multiply(ConstantValue right);

    ConstantValue divide(ConstantValue right);

    ConstantValue clone();
}
