/**
 * File: Expression.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 4:04:53 PM
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

import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;

public interface Expression {
    Expression evaluate(EvaluationContext context);
}
