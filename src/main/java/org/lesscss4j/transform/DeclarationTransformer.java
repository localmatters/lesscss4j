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

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.lesscss4j.error.ErrorUtils;
import org.lesscss4j.error.LessCssException;
import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.PositionAware;
import org.lesscss4j.model.expression.Expression;
import org.lesscss4j.transform.manager.TransformerManager;

public class DeclarationTransformer extends AbstractTransformer<Declaration> {

    public DeclarationTransformer() {
    }

    public DeclarationTransformer(TransformerManager transformerManager) {
        super(transformerManager);
    }

    public List<Declaration> transform(Declaration declaration, EvaluationContext context) {
        if (declaration.getValues() == null) return null;

        Declaration transformed = new Declaration(declaration, false);
        for (ListIterator<Object> iter = declaration.getValues().listIterator(); iter.hasNext();) {
            Object value = iter.next();
            value = transformDeclarationValue(value, declaration, context);
            transformed.addValue(value);
        }

        return Arrays.asList(transformed);
    }

    protected Object transformDeclarationValue(Object value, Declaration declaration, EvaluationContext context) {
        if (value instanceof Expression) {
            try {
                value = ((Expression) value).evaluate(context);
            }
            catch (LessCssException ex) {
                ErrorUtils.handleError(context.getErrorHandler(), (PositionAware) value, ex);
            }
        }
        return value;
    }
}
