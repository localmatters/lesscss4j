/**
 * File: Declaration.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 10:18:36 AM
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

import java.util.ArrayList;
import java.util.List;

import org.lesscss4j.model.expression.Expression;

public class Declaration extends AbstractElement implements DeclarationElement {
    private String _property;
    private List<Object> _values;
    private boolean _important = false;

    public Declaration() {
    }

    public Declaration(Declaration copy) {
        this(copy, true);
    }

    public Declaration(Declaration copy, boolean copyValues) {
        _property = copy._property;
        _important = copy._important;
        if (copyValues && copy._values != null) {
            _values = new ArrayList<Object>(copy._values.size());
            for (Object value : copy._values) {
                if (value instanceof Expression) {
                    _values.add(((Expression) value).clone());
                }
                else {
                    _values.add(value);
                }
            }
        }
    }

    public String getProperty() {
        return _property;
    }

    public void setProperty(String property) {
        _property = property;
    }

    public List<Object> getValues() {
        return _values;
    }

    public void setValues(List<Object> values) {
        _values = values;
    }

    public void addValue(Object value) {
        if (_values == null) {
            _values = new ArrayList<Object>();
        }
        _values.add(value);
    }

    public boolean isImportant() {
        return _important;
    }

    public void setImportant(boolean important) {
        _important = important;
    }

    public String getValuesAsString() {
        return getValuesAsString(new StringBuilder());
    }

    public String getValuesAsString(StringBuilder buf) {
        for (Object value : getValues()) {
            buf.append(value.toString());
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getProperty());
        buf.append(": ");
        getValuesAsString(buf);
        if (isImportant()) {
            buf.append(" !important");
        }
        return buf.toString();
    }

    @Override
    public Declaration clone() {
        return new Declaration(this);
    }
}
