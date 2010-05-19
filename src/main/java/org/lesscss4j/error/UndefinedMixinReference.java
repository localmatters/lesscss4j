/**
 * File: UndefinedMixinReference.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 10:26:09 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.error;

import org.lesscss4j.model.MixinReference;

public class UndefinedMixinReference extends LessCssException {
    private MixinReference _mixin;

    public UndefinedMixinReference(MixinReference mixin) {
        super(mixin.getSelector().getText());
        _mixin = mixin;
    }

    public UndefinedMixinReference(String message, MixinReference mixin) {
        super(message + ": " + mixin.getSelector().getText());
        _mixin = mixin;
    }

    public UndefinedMixinReference(String message, MixinReference mixin, Throwable cause) {
        super(message + ": " + mixin.getSelector().getText(), cause);
        _mixin = mixin;
    }

    public UndefinedMixinReference(MixinReference mixin, Throwable cause) {
        super(mixin.getSelector().getText(), cause);
        _mixin = mixin;
    }

    public MixinReference getMixin() {
        return _mixin;
    }
}
