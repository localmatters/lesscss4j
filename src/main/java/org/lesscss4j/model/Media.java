/**
 * File: Media.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:33:04 AM
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

public class Media extends BodyElementContainer implements BodyElement {
    private List<String> _mediums;

    public List<String> getMediums() {
        return _mediums;
    }

    public void setMediums(List<String> mediums) {
        _mediums = mediums;
    }

    public void addMedium(String medium) {
        if (_mediums == null) {
            _mediums = new ArrayList<String>();
        }
        _mediums.add(medium);
    }

}