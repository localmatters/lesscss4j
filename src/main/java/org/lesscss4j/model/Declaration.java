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

import java.util.List;

public class Declaration extends AbstractElement implements DeclarationElement {
    private String _property;
    private List<Object> _values;
    private boolean _important = false;
    private boolean _star = false; // ie specific thing...not sure what to call this

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

    public boolean isStar() {
        return _star;
    }

    public void setStar(boolean star) {
        _star = star;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (isStar()) {
            buf.append('*');
        }
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
}
