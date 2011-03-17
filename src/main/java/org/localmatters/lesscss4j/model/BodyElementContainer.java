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
package org.localmatters.lesscss4j.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.localmatters.lesscss4j.model.expression.Expression;

public class BodyElementContainer extends AbstractElement implements VariableContainer, RuleSetContainer {
    private List<BodyElement> _bodyElements = new ArrayList<BodyElement>();
    private Map<String, Expression> _variables = new LinkedHashMap<String, Expression>();
    private Map<Selector, List<RuleSet>> _ruleSetMap = new LinkedHashMap<Selector, List<RuleSet>>();
    private int _ruleSetCount;

    public BodyElementContainer() {
    }

    public BodyElementContainer(BodyElementContainer copy) {
        this(copy, true);
    }

    public BodyElementContainer(BodyElementContainer copy, boolean copyBodyElements) {
        super(copy);
        for (Map.Entry<String, Expression> entry : copy._variables.entrySet()) {
            _variables.put(entry.getKey(), entry.getValue().clone());
        }
        if (copyBodyElements) {
            for (BodyElement element : copy._bodyElements) {
                if (element instanceof RuleSet) {
                    element = ((RuleSet) element).clone();
                }
                addBodyElement(element);
            }
        }
    }

    public List<BodyElement> getBodyElements() {
        return _bodyElements;
    }

    public void addBodyElement(BodyElement bodyElement) {
        addBodyElement(bodyElement, -1);
    }

    public void addBodyElement(BodyElement bodyElement, int index) {
        if (index >= 0) {
            _bodyElements.add(Math.min(_bodyElements.size(), index), bodyElement);
        }
        else {
            _bodyElements.add(bodyElement);
        }
        if (bodyElement instanceof RuleSet) {
            RuleSet ruleSet = (RuleSet) bodyElement;
            for (Selector selector : ruleSet.getSelectors()) {
                List<RuleSet> ruleSetList = _ruleSetMap.get(selector);
                if (ruleSetList == null) {
                    ruleSetList = new ArrayList<RuleSet>();
                    _ruleSetMap.put(selector, ruleSetList);
                }
                ruleSetList.add(ruleSet);
            }
            _ruleSetCount++;
        }
    }

    public void clearBodyElements() {
        _ruleSetCount = 0;
        _ruleSetMap.clear();
        _bodyElements.clear();
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

    public void addRuleSet(RuleSet ruleSet, int index) {
        addBodyElement(ruleSet, index);
    }

    public List<RuleSet> getRuleSet(Selector selector) {
        return _ruleSetMap.get(selector);
    }

    public int getRuleSetCount() {
        return _ruleSetCount;
    }
}
