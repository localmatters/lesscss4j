/**
 * File: MixinReference.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 9:59:58 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.model;

public class MixinReference extends AbstractElement implements DeclarationElement {
    private Selector _selector;

    public Selector getSelector() {
        return _selector;
    }

    public void setSelector(Selector selector) {
        _selector = selector;
    }

    @Override
    public String toString() {
        return getSelector().toString();
    }
}
