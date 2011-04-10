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

import java.util.Arrays;
import java.util.List;

import org.localmatters.lesscss4j.model.BodyElement;
import org.localmatters.lesscss4j.model.Keyframes;
import org.localmatters.lesscss4j.model.RuleSet;
import org.localmatters.lesscss4j.transform.manager.TransformerManager;

public class KeyframesTransformer extends AbstractTransformer<Keyframes> {
    public KeyframesTransformer(TransformerManager transformerManager) {
        super(transformerManager);
    }

    public KeyframesTransformer() {
    }

    public List<Keyframes> transform(Keyframes keyframes, EvaluationContext context) {
        Keyframes transformed = new Keyframes(keyframes, false);
        evaluateVariables(keyframes, transformed, context);
        transformBodyElements(keyframes, transformed, context);
        return Arrays.asList(transformed);
    }

    protected void transformBodyElements(Keyframes keyframes, Keyframes transformed, EvaluationContext context) {
        EvaluationContext mediaContext = new EvaluationContext();
        mediaContext.setParentContext(context);
        mediaContext.setVariableContainer(transformed);
        mediaContext.setRuleSetContainer(transformed);

        for (BodyElement element : keyframes.getBodyElements()) {
            if (element instanceof RuleSet) {
                RuleSet ruleSet = (RuleSet) element;
                List<RuleSet> transformedRuleSets = getTransformer(ruleSet).transform(ruleSet, mediaContext);
                if (transformedRuleSets != null) {
                    for (RuleSet transformedRuleSet : transformedRuleSets) {
                        transformed.addBodyElement(transformedRuleSet);
                    }
                }
            }
            else {
                throw new IllegalStateException(
                    "Unexpected body element " + element.getClass().getSimpleName() + " in Media");
            }
        }
    }
}
