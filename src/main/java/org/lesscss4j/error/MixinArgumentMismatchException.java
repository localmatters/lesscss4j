/**
 * File: MixinArgumentMismatchException.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 20, 2010
 * Creation Time: 2:50:26 PM
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
import org.lesscss4j.model.RuleSet;

public class MixinArgumentMismatchException extends LessCssException {
    private MixinReference _reference;
    private RuleSet _mixin;

    public MixinArgumentMismatchException(MixinReference ref, RuleSet mixin) {
        super("Mixin argument mismatch. " +
              "Expected maximum of " + mixin.getArguments().size() + " but got " + ref.getArguments().size() + '.');
        _reference = ref;
        _mixin = mixin;
    }

    public MixinReference getReference() {
        return _reference;
    }

    public RuleSet getMixin() {
        return _mixin;
    }
}
