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

import java.util.Iterator;

import org.lesscss4j.error.ErrorUtils;
import org.lesscss4j.error.LessCssException;
import org.lesscss4j.model.VariableContainer;
import org.lesscss4j.model.expression.Expression;

public abstract class AbstractTransformer<T> implements Transformer<T> {
    protected void evaluateVariables(VariableContainer variableContainer,
                                     VariableContainer transformed,
                                     EvaluationContext context) {
        EvaluationContext varContext = new EvaluationContext(variableContainer, context);
        for (Iterator<String> iter = variableContainer.getVariableNames(); iter.hasNext(); ) {
            String varName = iter.next();
            Expression varExpression = variableContainer.getVariable(varName);
            try {
                transformed.setVariable(varName, varExpression.evaluate(varContext));
            }
            catch (LessCssException ex) {
                ErrorUtils.handleError(context.getErrorHandler(), varExpression, null, ex);
            }
        }
    }
}
