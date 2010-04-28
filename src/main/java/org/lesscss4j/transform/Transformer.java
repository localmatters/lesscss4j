/**
 * File: Transformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:19:12 AM
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

import org.lesscss4j.model.expression.EvaluationContext;

public interface Transformer<T> {
    void transform(T value, EvaluationContext context);
}
