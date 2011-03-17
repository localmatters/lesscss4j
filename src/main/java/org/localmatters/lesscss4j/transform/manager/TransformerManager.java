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

package org.localmatters.lesscss4j.transform.manager;

import org.localmatters.lesscss4j.transform.Transformer;

/**
 * This interface defines a way to locate a {@link Transformer} instance for a particular object.
 */
public interface TransformerManager {
    /**
     * Return a transformer for a particular object.
     *
     * @param The object to transform
     * @return The transformer for the given object.
     */
    <T> Transformer<T> getTransformer(T object);
}
