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

public class Declaration {
    private String _property;
    private String _value;
    private boolean _important = false;
    private boolean _star = false; // ie specific thing...not sure what to call this

    public String getProperty() {
        return _property;
    }

    public void setProperty(String property) {
        _property = property;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
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
}
