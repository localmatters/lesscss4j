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

import java.util.Map;

public class EvaluationContext {
    private EvaluationContext _parentContext;
    private Map<String, Expression> _variables;

    public EvaluationContext getParentContext() {
        return _parentContext;
    }

    public void setParentContext(EvaluationContext parentContext) {
        _parentContext = parentContext;
    }

    public Map<String, Expression> getVariables() {
        return _variables;
    }

    public void setVariables(Map<String, Expression> variables) {
        _variables = variables;
    }

    public Expression getVariableExpression(String name) {
        Expression value = getVariables().get(name);
        if (value == null && getParentContext() != null) {
            value = getParentContext().getVariableExpression(name);
        }
        return value;
    }
}
