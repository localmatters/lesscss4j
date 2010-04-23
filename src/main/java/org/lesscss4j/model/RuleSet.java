/**
 * File: RuleSet.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 10:16:36 AM
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


public class RuleSet extends DeclarationContainer implements BodyElement {
    private List<Selector> _selectors;

    public List<Selector> getSelectors() {
        return _selectors;
    }

    public void setSelectors(List<Selector> selectors) {
        _selectors = selectors;
    }

    public void addSelector(Selector selector) {
        if (_selectors == null) {
            _selectors = new ArrayList<Selector>();
        }
        _selectors.add(selector);
    }

}
