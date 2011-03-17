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
package org.localmatters.lesscss4j.transform;

import java.util.List;

import org.localmatters.lesscss4j.error.ErrorHandler;
import org.localmatters.lesscss4j.model.RuleSet;
import org.localmatters.lesscss4j.model.RuleSetContainer;
import org.localmatters.lesscss4j.model.Selector;
import org.localmatters.lesscss4j.model.VariableContainer;
import org.localmatters.lesscss4j.model.expression.Expression;

public class EvaluationContext {
    private EvaluationContext _parentContext;
    private VariableContainer _variableContainer;
    private RuleSetContainer _ruleSetContainer;
    private ErrorHandler _errorHandler;

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

    public Expression getVariable(String name) {
        Expression value = null;
        if (getVariableContainer() != null) {
            value = getVariableContainer().getVariable(name);
        }
        if (value == null && getParentContext() != null) {
            value = getParentContext().getVariable(name);
        }
        return value;
    }

    public List<RuleSet> getRuleSet(Selector selector) {

        List<RuleSet> ruleSet = null;
        if (getRuleSetContainer() != null) {
            ruleSet = getRuleSetContainer().getRuleSet(selector);
        }
        if ((ruleSet == null || ruleSet.size() == 0) && getParentContext() != null) {
            ruleSet = getParentContext().getRuleSet(selector);
        }
        return ruleSet;
    }

    public ErrorHandler getErrorHandler() {
        if (_errorHandler != null) {
            return _errorHandler;
        }
        else if (getParentContext() != null) {
            return getParentContext().getErrorHandler();
        }
        else {
            return null;
        }
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        _errorHandler = errorHandler;
    }
}
