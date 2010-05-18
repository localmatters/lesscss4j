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

public class Declaration extends AbstractElement implements DeclarationElement {
    private String _property;
    private List<Object> _values;
    private boolean _important = false;

    public Declaration() {
    }

    public Declaration(Declaration copy) {
        _property = copy._property;
        _important = copy._important;
        if (copy._values != null) {
            _values = new ArrayList<Object>(copy._values); // todo: needs to be a deep copy?
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

    public boolean isImportant() {
        return _important;
    }

    public void setImportant(boolean important) {
        _important = important;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getProperty());
        buf.append(": ");
        for (Object value : getValues()) {
            buf.append(value.toString());
        }
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
