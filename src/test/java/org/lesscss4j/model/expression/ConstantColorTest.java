/**
 * File: ConstantColorTest.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 9:32:50 AM
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

import junit.framework.TestCase;

public class ConstantColorTest extends TestCase {
    protected void validateColor(int expectedValue, ConstantColor actual) {
        assertEquals("Unexpected color value", expectedValue, (int)actual.getValue());
    }

    public void testParse() {
        validateColor(0xfed412, new ConstantColor("#fed412"));
        validateColor(0xffeedd, new ConstantColor("#fed"));
    }

    public void testToString() {
        assertEquals("#ab12de", new ConstantColor(0xab12de).toString());
        assertEquals("#001234", new ConstantColor(0x001234).toString());
        assertEquals("#ab1", new ConstantColor(0xaabb11).toString());
    }
}
