/**
 * File: VariableContainerImpl.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 11:39:46 AM
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.lesscss4j.transform.EvaluationContext;
import org.lesscss4j.model.expression.Expression;

public class VariableContainerImpl implements VariableContainer {
    private Map<String, Expression> _variables = new LinkedHashMap<String, Expression>();

    public VariableContainerImpl() {
        this(null);
    }

    public VariableContainerImpl(VariableContainer variableContainer) {
        this(variableContainer, null);
    }

    public VariableContainerImpl(VariableContainer variableContainer, EvaluationContext parentContext) {
        Map<String, Expression> evaluatedVariables = new HashMap<String, Expression>();

        EvaluationContext context = new EvaluationContext(variableContainer, parentContext);
        for (Iterator<String> iter = variableContainer.getVariableNames(); iter.hasNext(); ) {
            String varName = iter.next();
            evaluatedVariables.put(varName,
                                   variableContainer.getVariable(varName).evaluate(context));

        }
        _variables = evaluatedVariables;
    }

    public Iterator<String> getVariableNames() {
        return _variables.keySet().iterator();
    }
    
    public void setVariable(String name, Expression value) {
        _variables.put(name, value);
    }

    public Expression getVariable(String name) {
        return _variables.get(name);
    }
}
