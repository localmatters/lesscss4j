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
package org.lesscss4j.model.expression;

import org.lesscss4j.model.VariableContainer;

public class EvaluationContext {
    private EvaluationContext _parentContext;
    private VariableContainer _variableContainer;

    public EvaluationContext() {
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

    public Expression getVariableExpression(String name) {
        Expression value = getVariableContainer().getVariable(name);
        if (value == null && getParentContext() != null) {
            value = getParentContext().getVariableExpression(name);
        }
        return value;
    }
}
