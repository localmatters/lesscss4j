/**
 * File: ClassTransformerManager.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Aug 4, 2010
 * Creation Time: 9:10:03 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.transform.manager;

import java.util.Map;

import org.lesscss4j.transform.Transformer;

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
