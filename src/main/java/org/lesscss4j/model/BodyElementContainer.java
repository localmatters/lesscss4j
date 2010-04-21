/**
 * File: BodyElementContainer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:32:23 AM
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

public class BodyElementContainer {
    private List<BodyElement> _bodyElements;

    public List<BodyElement> getBodyElements() {
        return _bodyElements;
    }

    public void setBodyElements(List<BodyElement> bodyElements) {
        _bodyElements = bodyElements;
    }

    public void addBodyElement(BodyElement bodyElement) {
        if (_bodyElements == null) {
            _bodyElements = new ArrayList<BodyElement>();
        }
        _bodyElements.add(bodyElement);
    }
}
