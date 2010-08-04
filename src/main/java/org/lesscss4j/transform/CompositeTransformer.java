/**
 * File: CompositeTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Aug 4, 2010
 * Creation Time: 10:21:30 AM
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

import java.util.ArrayList;
import java.util.List;

public class CompositeTransformer<T> implements Transformer<T> {
    private List<Transformer<T>> _transformers;

    public List<Transformer<T>> getTransformers() {
        return _transformers;
    }

    public void setTransformers(List<Transformer<T>> transformers) {
        _transformers = transformers;
    }

    public List<T> transform(T value, EvaluationContext context) {
        List<T> transformed = new ArrayList<T>();
        transformed.add(value);

        for (Transformer<T> transformer : getTransformers()) {
            for (int idx = 0; idx < transformed.size(); idx++) {
                T val = transformed.get(idx);
                List<T> result = transformer.transform(val, context);
                if (result != null && result.size() > 0) {
                    transformed.set(idx, result.get(0));
                    if (result.size() > 1) {
                        for (int tdx = 1; tdx < result.size(); tdx++) {
                            transformed.add(idx + tdx, result.get(tdx));
                        }
                        idx += result.size() - 1;
                    }
                }
            }
        }
        return transformed;
    }
}
