/**
 * File: AbstractTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:27:50 AM
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lesscss4j.error.ErrorUtils;
import org.lesscss4j.error.LessCssException;
import org.lesscss4j.model.PositionAware;
import org.lesscss4j.model.VariableContainer;
import org.lesscss4j.model.expression.Expression;

public abstract class AbstractTransformer<T> implements Transformer<T> {
    protected void evaluateVariables(VariableContainer variableContainer, EvaluationContext context) {
        Map<String, Expression> evaluatedVariables = new HashMap<String, Expression>();

        EvaluationContext varContext = new EvaluationContext(variableContainer, context);
        for (Iterator<String> iter = variableContainer.getVariableNames(); iter.hasNext(); ) {
            String varName = iter.next();
            Expression varExpression = variableContainer.getVariable(varName);
            try {
                evaluatedVariables.put(varName, varExpression.evaluate(varContext));
            }
            catch (LessCssException ex) {
                ErrorUtils.handleError(context.getErrorHandler(), (PositionAware) varExpression, null, ex);
            }
        }

        for (Map.Entry<String, Expression> entry : evaluatedVariables.entrySet()) {
            String varName = entry.getKey();
            variableContainer.setVariable(varName, entry.getValue());
        }
    }
}
