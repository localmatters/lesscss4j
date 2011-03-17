/*
   Copyright 2010-present Local Matters, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.localmatters.lesscss4j.transform;

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
