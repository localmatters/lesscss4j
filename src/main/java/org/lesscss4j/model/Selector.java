/**
 * File: Selector.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:47:17 AM
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

public class Selector {
    String _text;

    public Selector() {
        this(null);
    }

    public Selector(String text) {
        _text = text;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
}
