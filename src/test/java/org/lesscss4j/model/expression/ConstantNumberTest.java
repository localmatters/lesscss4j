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

import org.lesscss4j.error.UnitMismatchException;

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
        assertEquals(".556px", new ConstantNumber(0.5556, "px").toString());
        assertEquals("12.5px", new ConstantNumber(12.5, "px").toString());
        assertEquals("3.01px", new ConstantNumber(3.0101, "px").toString());
        assertEquals("0", new ConstantNumber(0.0, null).toString());
        assertEquals("0", new ConstantNumber(0.0, "px").toString());
        assertEquals("0", new ConstantNumber(-0.0, "px").toString());
        assertEquals("-3.1em", new ConstantNumber(-3.1, "em").toString());
    }

/*
    public void testToStringPerformance() {
        Random random = new Random();
        double[] values = new double[100];
        for (int idx = 0; idx < values.length; idx++) {
            values[idx] = random.nextDouble() * 10;
        }

        StopWatch timer = new StopWatch();
        timer.setKeepTaskList(true);
        timer.start("DecimalFormat");
        perfTest(values, true);
        timer.stop();

        timer.start("Double.toString + cleanup");
        perfTest(values, false);
        timer.stop();

        System.out.println(timer.prettyPrint());
    }
    
    protected void perfTest(double[] values, boolean decimalFormat) {
        int ITERATIONS = 10000;
        for (int idx = 0; idx < ITERATIONS; idx++) {
            for (double value : values) {
                new ConstantNumber(value, null).toString(decimalFormat);
            }
        }
    }
*/

    public void testAdd() {
        assertEquals(new ConstantNumber(5, "px"), new ConstantNumber(3, null).add(new ConstantNumber(2, "px")));
        assertEquals(new ConstantNumber(5, "px"), new ConstantNumber(3, "px").add(new ConstantNumber(2, "px")));
        assertEquals(new ConstantNumber(5, "px"), new ConstantNumber(3, "px").add(new ConstantNumber(2, null)));
        assertEquals(new ConstantNumber(5, null), new ConstantNumber(3, null).add(new ConstantNumber(2, null)));
    }

    public void testAddUnitMismatch() {
        try {
            new ConstantNumber(3, "px").add(new ConstantNumber(2, "em"));
            fail("expected UnitMismatchException");
        }
        catch (UnitMismatchException ex) {
            // expected
        }
    }

    public void testAddColor() {
        try {
            new ConstantNumber(3, "px").add(new ConstantColor(0xffffff));
            fail("expected exception");
        }
        catch (UnitMismatchException ex) {
            // expected
        }
    }

    public void testSubtract() {
        assertEquals(new ConstantNumber(1, "px"), new ConstantNumber(3, null).subtract(new ConstantNumber(2, "px")));
        assertEquals(new ConstantNumber(1, "px"), new ConstantNumber(3, "px").subtract(new ConstantNumber(2, "px")));
        assertEquals(new ConstantNumber(1, "px"), new ConstantNumber(3, "px").subtract(new ConstantNumber(2, null)));
        assertEquals(new ConstantNumber(1, null), new ConstantNumber(3, null).subtract(new ConstantNumber(2, null)));
    }

    public void testSubtractUnitMismatch() {
        try {
            new ConstantNumber(3, "px").subtract(new ConstantNumber(2, "em"));
            fail("expected UnitMismatchException");
        }
        catch (UnitMismatchException ex) {
            // expected
        }
    }

    public void testSubtractColor() {
        try {
            new ConstantNumber(3, "px").subtract(new ConstantColor(0xffffff));
            fail("expected exception");
        }
        catch (UnitMismatchException ex) {
            // expected
        }
    }
    
    public void testDivide() {
        assertEquals(new ConstantNumber(1.5, "px"), new ConstantNumber(3, null).divide(new ConstantNumber(2, "px")));
        assertEquals(new ConstantNumber(1.5, "px"), new ConstantNumber(3, "px").divide(new ConstantNumber(2, "px")));
        assertEquals(new ConstantNumber(1.5, "px"), new ConstantNumber(3, "px").divide(new ConstantNumber(2, null)));
        assertEquals(new ConstantNumber(1.5, null), new ConstantNumber(3, null).divide(new ConstantNumber(2, null)));
    }

    public void testDivideUnitMismatch() {
        try {
            new ConstantNumber(3, "px").divide(new ConstantNumber(2, "em"));
            fail("expected UnitMismatchException");
        }
        catch (UnitMismatchException ex) {
            // expected
        }
    }

    public void testDivideColor() {
        try {
            new ConstantNumber(3, "px").divide(new ConstantColor(0xffffff));
            fail("expected exception");
        }
        catch (UnitMismatchException ex) {
            // expected
        }
    }

    public void testMultiply() {
        assertEquals(new ConstantNumber(6, "px"), new ConstantNumber(3, null).multiply(new ConstantNumber(2, "px")));
        assertEquals(new ConstantNumber(6, "px"), new ConstantNumber(3, "px").multiply(new ConstantNumber(2, "px")));
        assertEquals(new ConstantNumber(6, "px"), new ConstantNumber(3, "px").multiply(new ConstantNumber(2, null)));
        assertEquals(new ConstantNumber(6, null), new ConstantNumber(3, null).multiply(new ConstantNumber(2, null)));
    }

    public void testMultiplyUnitMismatch() {
        try {
            new ConstantNumber(3, "px").multiply(new ConstantNumber(2, "em"));
            fail("expected UnitMismatchException");
        }
        catch (UnitMismatchException ex) {
            // expected
        }
    }

    public void testMultiplyColor() {
        assertEquals(new ConstantColor(0x333333), new ConstantNumber(3, null).multiply(new ConstantColor(0x111111)));
        assertEquals(new ConstantColor(0xffffff), new ConstantNumber(3, null).multiply(new ConstantColor(0xaaaaaa)));
    }
}
