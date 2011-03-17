/**
 * File: PageTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:28:24 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
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