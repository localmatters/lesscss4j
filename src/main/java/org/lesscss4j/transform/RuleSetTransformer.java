/**
 * File: PageTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:28:24 AM
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

import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.expression.EvaluationContext;

public class RuleSetTransformer extends AbstractDeclarationContainerTransformer<RuleSet> {
    public void transform(RuleSet page, EvaluationContext context) {
        evaluateVariables(page, context);
        transformDeclarations(page, context);
    }
}