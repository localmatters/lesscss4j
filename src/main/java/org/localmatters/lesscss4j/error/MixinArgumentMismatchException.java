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

package org.localmatters.lesscss4j.error;

import org.localmatters.lesscss4j.model.MixinReference;
import org.localmatters.lesscss4j.model.RuleSet;

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
