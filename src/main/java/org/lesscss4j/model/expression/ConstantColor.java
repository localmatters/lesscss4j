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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lesscss4j.error.DivideByZeroException;
import org.lesscss4j.error.UnitMismatchException;

public class ConstantColor implements ConstantValue {
    private int _red;
    private int _green;
    private int _blue;
    private Float _alpha;

    /** Regular expression that is used to extract the rgb or hsl and alpha values from the color function value */
    public static final Pattern RGB_HSL_PATTERN = Pattern.compile(
        "(?i)(?:rgb|hsl)a?\\s*\\(\\s*(-?\\d+%?)\\s*,\\s*(-?\\d+%?)\\s*,\\s*(-?\\d+%?)(?:\\s*,\\s*(-?(?:\\d+(?:\\.\\d*)*|\\.\\d+%?)))?\\s*\\)");

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

    public ConstantColor(ConstantColor copy) {
        _red = copy._red;
        _green = copy._green;
        _blue = copy._blue;
        _alpha = copy._alpha;
    }

    public ConstantColor(int value) {
        setValue(value);
    }

    public ConstantColor(int red, int green, int blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
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
        else if (isRGBFunction(value)) {
            Matcher matcher = RGB_HSL_PATTERN.matcher(value);
            if (matcher.matches()) {
                String red = matcher.group(1);
                String green = matcher.group(2);
                String blue = matcher.group(3);

                setRed(parseRGBValue(red));
                setGreen(parseRGBValue(green));
                setBlue(parseRGBValue(blue));

                if (value.charAt(3) == 'a' || value.charAt(3) == 'A') {
                    String alpha = matcher.group(4);
                    setAlpha(Float.parseFloat(alpha));
                }
            }
            else {
                throw new IllegalArgumentException("Invalid RGB color specification: " + value);
            }
        }
        else if (isHSLFunction(value)) {
            Matcher matcher = RGB_HSL_PATTERN.matcher(value);
            if (matcher.matches()) {
                if (value.charAt(3) == 'a' || value.charAt(3) == 'A') {
                    String alpha = matcher.group(4);
                    setAlpha(Float.parseFloat(alpha));
                }

                float hue = Integer.parseInt(matcher.group(1));

                // Normalize hue to the range [0, 360)
                if (hue < 0 || hue >= 360) {
                    hue = ((hue % 360) + 360) % 360;
                }

                // Convert hue into the range [0, 1]
                hue /= 360.0;

                float saturation = parsePercentage(matcher.group(2));
                float lightness = parsePercentage(matcher.group(3));

                int[] rgb = hslToRgb(hue, saturation, lightness);
                setRed(rgb[0]);
                setGreen(rgb[1]);
                setBlue(rgb[2]);
            }
            else {
                throw new IllegalArgumentException("Invalid HSL color specification: " + value);
            }
        }
        else {
            Integer keywordValue = COLOR_KEYWORDS.get(value.toLowerCase());
            if (keywordValue != null) {
                setValue(keywordValue);
            }
        }
    }

    /**
     * Converts an HSL color value to RGB. Conversion formula adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes h, s, and l are contained in the set [0, 1] and returns r, g, and b in the set [0, 255].
     *
     * @param h The hue
     * @param s The saturation
     * @param l The lightness
     * @return Integer array with the RGB representation.  0=red, 1=green, 2=blue
     */
    protected static int[] hslToRgb(float h, float s, float l) {
        float r, g, b;

        if (s == 0) {
            r = g = b = l; // achromatic
        }
        else {

            float q = l < 0.5f ? l * (1f + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1f / 3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f / 3f);
        }

        return new int[]{Math.round(r * 255f), Math.round(g * 255f), Math.round(b * 255f)};
    }

    protected static float hueToRgb(float p, float q, float t) {
        if (t < 0f) t += 1.0;
        if (t > 1f) t -= 1.0;
        if (t < 1f / 6f) return p + (q - p) * 6f * t;
        if (t < 1f / 2f) return q;
        if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6f;
        return p;
    }

    protected float parsePercentage(String value) {
        if (value.endsWith("%")) {
            // Strip off the optional percent sign
            value = value.substring(0, value.length() - 1);
        }
        return Math.max(0, Math.min(100, Integer.parseInt(value))) / 100.0f;
    }

    protected int parseRGBValue(String value) {
        if (value.charAt(value.length() - 1) == '%') {
            // colors are in terms of percentage
            return (int) (parsePercentage(value) * 255);
        }
        else {
            return Integer.parseInt(value);
        }
    }

    public void setValue(int value) {
        setRed((value & 0xff0000) >> 16);
        setGreen((value & 0x00ff00) >> 8);
        setBlue(value & 0x0000ff);
    }

    public double getValue() {
        return (getRed() << 16) | (getGreen() << 8) | getBlue();
    }

    protected void checkUnits(ConstantValue that) {
        if (that instanceof ConstantNumber && ((ConstantNumber) that).getUnit() != null) {
            throw new UnitMismatchException(this, that);
        }
    }

    public ConstantValue add(ConstantValue right) {
        checkUnits(right);
        if (right instanceof ConstantNumber) {
            return new ConstantColor((int) (getRed() + right.getValue()),
                                     (int) (getGreen() + right.getValue()),
                                     (int) (getBlue() + right.getValue()));
        }
        else {
            ConstantColor color = (ConstantColor) right;
            return new ConstantColor(getRed() + color.getRed(),
                                     getGreen() + color.getGreen(),
                                     getBlue() + color.getBlue());
        }
    }


    public ConstantValue subtract(ConstantValue right) {
        checkUnits(right);
        if (right instanceof ConstantNumber) {
            return new ConstantColor((int) (getRed() - right.getValue()),
                                     (int) (getGreen() - right.getValue()),
                                     (int) (getBlue() - right.getValue()));
        }
        else {
            ConstantColor color = (ConstantColor) right;
            return new ConstantColor(getRed() - color.getRed(),
                                     getGreen() - color.getGreen(),
                                     getBlue() - color.getBlue());
        }
    }

    public ConstantValue multiply(ConstantValue right) {
        checkUnits(right);
        return new ConstantColor((int) (getRed() * right.getValue()),
                                 (int) (getGreen() * right.getValue()),
                                 (int) (getBlue() * right.getValue()));
    }

    public ConstantValue divide(ConstantValue right) {
        checkUnits(right);
        if (right.getValue() == 0.0) {
            throw new DivideByZeroException();
        }
        return new ConstantColor((int) (getRed() / right.getValue()),
                                 (int) (getGreen() / right.getValue()),
                                 (int) (getBlue() / right.getValue()));
    }

    @Override
    public String toString() {
        if (getAlpha() != null) {
            DecimalFormat alphaFormat = new DecimalFormat("0.###");
            return "rgba(" + getRed() + ',' + getGreen() + ',' + getBlue() + ',' + alphaFormat.format(getAlpha()) + ')';
        }

        String str = String.format("#%06x", (int) getValue());

        // Shorten colors of the form #aabbcc to #abc
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        if (((r & 0xf0) >> 4) == (r & 0xf) &&
            ((g & 0xf0) >> 4) == (g & 0xf) &&
            ((b & 0xf0) >> 4) == (b & 0xf)) {
            str = "#" + str.charAt(1) + str.charAt(3) + str.charAt(5);
        }
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ConstantColor that = (ConstantColor) obj;

        return this.getRed() == that.getRed() &&
               this.getGreen() == that.getGreen() &&
               this.getBlue() == that.getBlue() &&
               (this.getAlpha() == null && that.getAlpha() == null ||
                this.getAlpha().equals(that.getAlpha()));

    }

    @Override
    public int hashCode() {
        return (int) getValue();
    }

    protected int pinColor(int value) {
        return Math.max(0, Math.min(value, 0xff));
    }

    public int getRed() {
        return _red;
    }

    public void setRed(int red) {
        _red = pinColor(red);
    }

    public int getGreen() {
        return _green;
    }

    public void setGreen(int green) {
        _green = pinColor(green);
    }

    public int getBlue() {
        return _blue;
    }

    public void setBlue(int blue) {
        _blue = pinColor(blue);
    }

    public Float getAlpha() {
        return _alpha;
    }

    public void setAlpha(Float alpha) {
        if (alpha != null) {
            _alpha = Math.min(1.0f, Math.max(0.0f, alpha));
        }
        else {
            _alpha = alpha;
        }
    }

    public static boolean isColorFunction(String value) {
        return isRGBFunction(value) || isHSLFunction(value);
    }

    public static boolean isRGBFunction(String value) {
        return value.length() >= 3 &&
               (value.charAt(0) == 'r' || value.charAt(0) == 'R') &&
               (value.charAt(1) == 'g' || value.charAt(1) == 'G') &&
               (value.charAt(2) == 'b' || value.charAt(2) == 'B');
    }

    public static boolean isHSLFunction(String value) {
        return value.length() >= 3 &&
               (value.charAt(0) == 'h' || value.charAt(0) == 'H') &&
               (value.charAt(1) == 's' || value.charAt(1) == 'S') &&
               (value.charAt(2) == 'l' || value.charAt(2) == 'L');
    }

    public ConstantColor clone() {
        return new ConstantColor(this);
    }
}