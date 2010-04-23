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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lesscss4j.model.expression.Expression;


public class RuleSet extends DeclarationContainer implements BodyElement, VariableContainer {
    private List<Selector> _selectors;
    private Map<String, Expression> _variables = new LinkedHashMap<String, Expression>();

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

    public Expression setVariable(String name, Expression value) {
        return _variables.put(name, value);
    }

    public Expression getVariable(String name) {
        return _variables.get(name);
    }

    public Iterator<String> getVariableNames() {
        return _variables.keySet().iterator();
    }
}
