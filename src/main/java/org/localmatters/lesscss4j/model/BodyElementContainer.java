/*
   Copyright 2010-present Local Matters, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
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
