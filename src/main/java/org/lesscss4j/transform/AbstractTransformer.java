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

import org.lesscss4j.model.VariableContainer;
import org.lesscss4j.model.VariableContainerImpl;

public abstract class AbstractTransformer<T> implements Transformer<T> {
    protected void evaluateVariables(VariableContainer variableContainer, EvaluationContext context) {
        VariableContainer evaluated =  new VariableContainerImpl(variableContainer, context);
        for (Iterator<String> iter = evaluated.getVariableNames(); iter.hasNext(); ) {
            String varName = iter.next();
            variableContainer.setVariable(varName, evaluated.getVariable(varName));
        }
    }
}
