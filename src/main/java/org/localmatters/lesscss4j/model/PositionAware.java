/**
 * File: PositionAware.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 27, 2010
 * Creation Time: 4:31:40 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.localmatters.lesscss4j.model;

public interface PositionAware {
    int getLine();
    int getChar();
}
