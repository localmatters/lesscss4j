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

import java.util.LinkedHashMap;
import java.util.Map;

import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.Media;
import org.lesscss4j.model.Page;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.StyleSheet;
import org.lesscss4j.transform.DeclarationTransformer;
import org.lesscss4j.transform.MediaTransformer;
import org.lesscss4j.transform.PageTransformer;
import org.lesscss4j.transform.RuleSetTransformer;
import org.lesscss4j.transform.StyleSheetTransformer;
import org.lesscss4j.transform.Transformer;

public class ClassTransformerManager implements TransformerManager {
    private Map<Class, Transformer> _classTransformerMap;

    public <T> Transformer<T> getTransformer(T object) {
        Class objClass = object.getClass();

        Map<Class, Transformer> transformerMap = getClassTransformerMap();
        Transformer<T> transformer = transformerMap.get(objClass);
        if (transformer == null) {
            for (Map.Entry<Class, Transformer> entry : transformerMap.entrySet()) {
                if (entry.getKey().isAssignableFrom(objClass)) {
                    transformer = entry.getValue();
                    break;
                }
            }
        }
        return transformer;
    }

    public Map<Class, Transformer> getClassTransformerMap() {
        if (_classTransformerMap == null) {
            setClassTransformerMap(createDefaultClassTransformerMap());
        }
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

    public Map<Class, Transformer> createDefaultClassTransformerMap() {
        Map<Class, Transformer> transformerMap = new LinkedHashMap<Class, Transformer>();
        transformerMap.put(Declaration.class, new DeclarationTransformer());
        transformerMap.put(RuleSet.class, new RuleSetTransformer());
        transformerMap.put(Page.class, new PageTransformer());
        transformerMap.put(Media.class, new MediaTransformer());
        transformerMap.put(StyleSheet.class, new StyleSheetTransformer());
        return transformerMap;
    }
}
