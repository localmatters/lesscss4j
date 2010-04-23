/**
 * File: ConstantNumber.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 8:33:13 AM
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

import java.util.HashMap;
import java.util.Map;

import org.lesscss4j.exception.UnitMismatchException;

public class ConstantColor implements ConstantValue {
    private int _value;

    private static final Map<String, Integer> COLOR_KEYWORDS = new HashMap<String, Integer>();
    static {
        COLOR_KEYWORDS.put("maroon", 0x800000);
        COLOR_KEYWORDS.put("red", 0xff0000);
        COLOR_KEYWORDS.put("orange", 0xffA500);
        COLOR_KEYWORDS.put("yellow", 0xffff00);
        COLOR_KEYWORDS.put("olive", 0x808000);
        COLOR_KEYWORDS.put("purple", 0x800080);
        COLOR_KEYWORDS.put("fuchsia", 0xff00ff);
        COLOR_KEYWORDS.put("white", 0xffffff);
        COLOR_KEYWORDS.put("lime", 0x00ff00);
        COLOR_KEYWORDS.put("green", 0x008000);
        COLOR_KEYWORDS.put("navy", 0x000080);
        COLOR_KEYWORDS.put("blue", 0x0000ff);
        COLOR_KEYWORDS.put("aqua", 0x00ffff);
        COLOR_KEYWORDS.put("teal", 0x008080);
        COLOR_KEYWORDS.put("black", 0x000000);
        COLOR_KEYWORDS.put("silver", 0xc0c0c0);
        COLOR_KEYWORDS.put("gray", 0x808080);
    }


    public ConstantColor() {
        this(0);
    }

    public ConstantColor(int value) {
        setValue(value);
    }

    public ConstantColor(String value) {
        if (value.charAt(0) == '#') {
            value = value.substring(1); // strip off the '#'
            if (value.length() == 3) {
                value = new String(new char[]{value.charAt(0),
                                              value.charAt(0),
                                              value.charAt(1),
                                              value.charAt(1),
                                              value.charAt(2),
                                              value.charAt(2)
                });
            }
            setValue(Integer.parseInt(value, 16));
        }
        else if ((value.charAt(0) == 'r' || value.charAt(0) == 'R') &&
                 (value.charAt(1) == 'g' || value.charAt(1) == 'G') &&
                 (value.charAt(2) == 'b' || value.charAt(2) == 'B')) {
            // todo: handle rgb(r, g, b) here
            throw new UnsupportedOperationException("rgb(r, g, b) color values not supported yet");
        }
        else {
            Integer keywordValue = COLOR_KEYWORDS.get(value.toLowerCase());
            if (keywordValue != null) {
                setValue(keywordValue);
            }
        }
    }

    public void setValue(int value) {
        _value = value;
    }

    public double getValue() {
        return _value;
    }

    protected void checkUnits(ConstantValue that) {
        if (that instanceof ConstantNumber && ((ConstantNumber) that).getUnit() != null) {
            throw new UnitMismatchException(this, that);
        }
    }

    // todo: verify that these operations are correct

    public ConstantValue add(ConstantValue right) {
        checkUnits(right);
        return new ConstantColor((int) (this.getValue() + right.getValue()));
    }


    public ConstantValue subtract(ConstantValue right) {
        checkUnits(right);
        return new ConstantColor((int) (this.getValue() - right.getValue()));
    }

    public ConstantValue multiply(ConstantValue right) {
        checkUnits(right);
        return new ConstantColor((int) (this.getValue() * right.getValue()));
    }

    public ConstantValue divide(ConstantValue right) {
        checkUnits(right);
        if (right.getValue() == 0.0) {
            // todo: throw exception
        }
        return new ConstantColor((int) (this.getValue() / right.getValue()));
    }

    @Override
    public String toString() {
        String str = String.format("#%06x", _value);

        // Shorten colors of the form #aabbcc to #abc
        if (((_value & 0xf00000) >> 4) == (_value & 0x0f0000) &&
            ((_value & 0x00f000) >> 4) == (_value & 0x000f00) &&
            ((_value & 0x0000f0) >> 4) == (_value & 0x00000f)) {
            str = "#" + str.charAt(1) + str.charAt(3) + str.charAt(5);
        }
        return str;
    }
}