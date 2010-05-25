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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lesscss4j.model.expression.Expression;


public class RuleSet extends DeclarationContainer implements BodyElement, Cloneable {
    private List<Selector> _selectors;
    private Map<String, Expression> _arguments = new LinkedHashMap<String, Expression>();

    public RuleSet() {
    }

    public RuleSet(RuleSet copy) {
        super(copy);
        if (copy._selectors != null) {
            _selectors = new ArrayList<Selector>(copy._selectors.size());
            for (Selector selector : copy._selectors) {
                _selectors.add(selector.clone());
            }
        }

        for (Map.Entry<String, Expression> entry : copy._arguments.entrySet()) {
            _arguments.put(entry.getKey(), entry.getValue().clone());
        }
    }

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

    public Map<String, Expression> getArguments() {
        return _arguments;
    }

    public void addArgument(String name, Expression value) {
        _arguments.put(name, value);
    }

    @Override
    public RuleSet clone() {
        return new RuleSet(this);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (Selector selector : _selectors) {
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(selector.toString());
        }
        return buf.toString();
    }
}
