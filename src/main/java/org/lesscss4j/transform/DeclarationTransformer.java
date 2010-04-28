/**
 * File: DeclarationTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:40:12 AM
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

import java.util.ListIterator;

import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.expression.Expression;

public class DeclarationTransformer implements Transformer<Declaration> {
    public void transform(Declaration declaration, EvaluationContext context) {
        for (ListIterator<Object> iter = declaration.getValues().listIterator(); iter.hasNext();) {
            Object value = iter.next();
            if (value instanceof Expression) {
                iter.set(((Expression) value).evaluate(context));
            }
        }
    }
}
