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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lesscss4j.model.expression.Expression;

public class BodyElementContainer extends AbstractElement implements VariableContainer {
    private List<BodyElement> _bodyElements;
    private Map<String, Expression> _variables = new LinkedHashMap<String, Expression>();

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

    public void setVariable(String name, Expression value) {
        _variables.put(name, value);
    }

    public Expression getVariable(String name) {
        return _variables.get(name);
    }

    public Iterator<String> getVariableNames() {
        return _variables.keySet().iterator();
    }
}
