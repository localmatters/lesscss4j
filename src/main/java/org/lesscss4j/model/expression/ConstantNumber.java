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

import org.lesscss4j.exception.UnitMismatchException;

public class ConstantNumber implements ConstantValue {
    private double _value;
    private String _unit;

    public ConstantNumber() {
        this(0, null);
    }

    public ConstantNumber(double value, String unit) {
        setValue(value);
        setUnit(unit);
    }

    public ConstantNumber(String value) {
        value = (value != null) ? value.trim() : value;
        if (value == null || value.length() == 0) {
            setValue(0);
            setUnit(null);
        }
        else {
            int unitIdx = -1;
            for (int numChars = value.length(), idx = numChars - 1; idx >= 0; idx--) {
                char ch = value.charAt(idx);
                if ('0' <= ch && ch <= '9' || ch == '.') {
                    unitIdx = idx + 1;
                    break;
                }
            }

            if (unitIdx >= 0) {
                setValue(Double.parseDouble(value.substring(0, unitIdx)));
                setUnit(value.substring(unitIdx));
            }
        }
    }

    public String getUnit() {
        return _unit;
    }

    public void setUnit(String unit) {
        _unit = unit != null ? unit.trim() : unit;
        if (_unit != null && _unit.length() == 0) {
            _unit = null;
        }
    }

    public void setValue(double value) {
        _value = value;
    }

    public double getValue() {
        return _value;
    }

    protected boolean hasCompatibleUnits(ConstantNumber that) {
        return this.getUnit() == null || that.getUnit() == null || this.getUnit().equals(that.getUnit());
    }

    protected void checkUnits(ConstantValue that) {
        if (!(that instanceof ConstantNumber) || !hasCompatibleUnits((ConstantNumber) that)) {
            throw new UnitMismatchException(this, that);
        }
    }

    protected String selectUnit(ConstantValue right) {
        return this.getUnit() != null ? this.getUnit() : ((ConstantNumber) right).getUnit();
    }

    public ConstantValue add(ConstantValue right) {
        checkUnits(right);
        return new ConstantNumber(this.getValue() + right.getValue(), selectUnit(right));
    }


    public ConstantValue subtract(ConstantValue right) {
        checkUnits(right);
        return new ConstantNumber(this.getValue() - right.getValue(), selectUnit(right));
    }

    public ConstantValue multiply(ConstantValue right) {
        checkUnits(right);
        return new ConstantNumber(this.getValue() * right.getValue(), selectUnit(right));
    }

    public ConstantValue divide(ConstantValue right) {
        checkUnits(right);
        if (right.getValue() == 0.0) {
            // todo: throw exception
        }
        return new ConstantNumber(this.getValue() / right.getValue(), selectUnit(right));
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("#.###" + (getUnit() != null ? "'" + getUnit() + "'" : ""));
        format.setMinimumIntegerDigits(0);
        format.setMinimumFractionDigits(0);
        return format.format(getValue());
    }
}
