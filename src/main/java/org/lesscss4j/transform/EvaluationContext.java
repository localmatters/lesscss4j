/**
 * File: EvaluationContext.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 8:44:20 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.transform;

import java.util.List;

import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.RuleSetContainer;
import org.lesscss4j.model.Selector;
import org.lesscss4j.model.VariableContainer;
import org.lesscss4j.model.expression.Expression;

public class EvaluationContext {
    private EvaluationContext _parentContext;
    private VariableContainer _variableContainer;
    private RuleSetContainer _ruleSetContainer;
    private int _ruleSetIndex = 0;

    public EvaluationContext() {
        this(null);
    }

    public EvaluationContext(VariableContainer variableContainer) {
        this(variableContainer, null);
    }

    public EvaluationContext(VariableContainer variableContainer, EvaluationContext parent) {
        setParentContext(parent);
        setVariableContainer(variableContainer);
    }

    public EvaluationContext getParentContext() {
        return _parentContext;
    }

    public void setParentContext(EvaluationContext parentContext) {
        _parentContext = parentContext;
    }

    public VariableContainer getVariableContainer() {
        return _variableContainer;
    }

    public void setVariableContainer(VariableContainer variableContainer) {
        _variableContainer = variableContainer;
    }

    public RuleSetContainer getRuleSetContainer() {
        return _ruleSetContainer;
    }

    public void setRuleSetContainer(RuleSetContainer ruleSetContainer) {
        _ruleSetContainer = ruleSetContainer;
    }

    public int getRuleSetIndex() {
        return _ruleSetIndex;
    }

    public void setRuleSetIndex(int ruleSetIndex) {
        _ruleSetIndex = ruleSetIndex;
    }

    public Expression getVariableExpression(String name) {
        Expression value = getVariableContainer().getVariable(name);
        if (value == null && getParentContext() != null) {
            value = getParentContext().getVariableExpression(name);
        }
        return value;
    }

    public List<RuleSet> getRuleSet(Selector selector) {
        List<RuleSet> ruleSet = getRuleSetContainer().getRuleSet(selector);
        if ((ruleSet == null || ruleSet.size() == 0) && getParentContext() != null) {
            ruleSet = getParentContext().getRuleSet(selector);
        }
        return ruleSet;
    }
}
