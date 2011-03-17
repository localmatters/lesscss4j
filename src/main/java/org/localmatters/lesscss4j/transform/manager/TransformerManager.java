/**
 * File: TransformerManager.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Aug 4, 2010
 * Creation Time: 9:07:54 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
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
