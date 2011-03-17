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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.localmatters.lesscss4j.model.RuleSet;
import org.localmatters.lesscss4j.transform.manager.TransformerManager;

public class RuleSetTransformer extends AbstractDeclarationContainerTransformer<RuleSet> {

    public RuleSetTransformer() {
    }

    public RuleSetTransformer(TransformerManager transformerManager) {
        super(transformerManager);
    }

    public List<RuleSet> transform(RuleSet ruleSet, EvaluationContext context) {
        RuleSet transformed;

        // Rule sets with arguments shouldn't be processed since they serve
        // only as a template for use by other rule sets in the stylesheet.
        List<RuleSet> ruleSetList;
        if (ruleSet.getArguments().size() > 0) {
            transformed = new RuleSet(ruleSet);
            ruleSetList = Arrays.asList(transformed);
        }
        else {
            transformed = new RuleSet(ruleSet, false);

            ruleSetList = new ArrayList<RuleSet>();
            ruleSetList.add(transformed);
            doTransform(ruleSet, ruleSetList, context);
        }
        return ruleSetList;
    }
}
