/**
 * File: VariableContainer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 11:26:29 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.model;

import java.util.Iterator;

import org.lesscss4j.model.expression.Expression;

public interface VariableContainer {
    Expression getVariable(String name);
    void setVariable(String name, Expression value);
    Iterator<String> getVariableNames();
}
