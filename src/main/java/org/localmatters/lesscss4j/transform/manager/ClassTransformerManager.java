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

import java.util.Map;

import org.localmatters.lesscss4j.transform.Transformer;

/**
 * Implementation of the {@link TransformerManager} that looks up {@link Transformer} instances based on the class of
 * the provided object.  Instances of this class are provided with a <code>Map</code> that provides a mapping between
 * <code>Class</code> and {@link Transformer} instances.  The {@link Transformer} to use for a particular object is
 * determined as follows:
 * <p/>
 * <ol>
 * <li>If there is a <code>Class</code> in the given Map that matches the given object's <code>Class</code> exactly,
 *     the associated {@link Transformer} is returned.</li>
 * <li>Iterate over the entries in the given <code>Map</code>.  For the first entry where the <code>Class</code> is a
 *     superclass or interface implemented by the given object, the associated {@link Transformer} is returned.</li>
 * </ol>
 * <p/>
 * As a result, it is highly recommended that the map provided to instances of this class return values from the
 * <code>entrySet</code> method in a consistent way. (i.e. something like <code>LinkedHashMap</code>)
 */
public class ClassTransformerManager implements TransformerManager {
    private Map<Class, Transformer> _classTransformerMap;

    /**
     * Find the transformer for the given object.  The algorithm used is described in the description of this class.
     *
     * @return The located transformer.  <code>null</code> if no matching transformer can be found.
     */
    public <T> Transformer<T> getTransformer(T object) {
        Class objClass = object.getClass();

        Transformer<T> transformer = null;

        Map<Class, Transformer> transformerMap = getClassTransformerMap();
        if (transformerMap != null) {
            transformer = transformerMap.get(objClass);
            if (transformer == null) {
                for (Map.Entry<Class, Transformer> entry : transformerMap.entrySet()) {
                    if (entry.getKey().isAssignableFrom(objClass)) {
                        transformer = entry.getValue();
                        break;
                    }
                }
            }
        }
        return transformer;
    }

    public Map<Class, Transformer> getClassTransformerMap() {
        return _classTransformerMap;
    }

    public void setClassTransformerMap(Map<Class, Transformer> classTransformerMap) {
        for (Transformer transformer : classTransformerMap.values()) {
            if (transformer instanceof TransformerManagerAware) {
                ((TransformerManagerAware) transformer).setTransformerManager(this);
            }
        }
        _classTransformerMap = classTransformerMap;
    }

}
