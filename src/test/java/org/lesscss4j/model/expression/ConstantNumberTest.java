/**
 * File: ConstantNumberTest.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 8:50:49 AM
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

public class ConstantNumberTest extends TestCase {
    protected void validateNumber(double expectedValue, String expectedUnit, ConstantNumber actual) {
        assertEquals("Unexpected value", expectedValue, actual.getValue());
        assertEquals("Unexpected unit", expectedUnit, actual.getUnit());
    }

    public void testParseNoUnits() {
        validateNumber(5.0, null, new ConstantNumber("5"));
        validateNumber(3.0, null, new ConstantNumber("3.0"));
        validateNumber(3.0, null, new ConstantNumber("3."));
        validateNumber(-3.0, null, new ConstantNumber("-3"));
        validateNumber(0.5, null, new ConstantNumber(".5"));
    }

    public void testParseWithUnits() {
        validateNumber(5.0, "px", new ConstantNumber("5px"));
        validateNumber(3.0, "%", new ConstantNumber("3.0%"));
        validateNumber(3.0, "em", new ConstantNumber("3.em"));
        validateNumber(-5.0, "px", new ConstantNumber("-5px"));
        validateNumber(0.5, "pt", new ConstantNumber("0.5pt"));
    }
    
    public void testToString() {
        assertEquals("3", new ConstantNumber(3, null).toString());
        assertEquals("3.1em", new ConstantNumber(3.1, "em").toString());
        assertEquals(".5px", new ConstantNumber(0.5, "px").toString());
    }
}
