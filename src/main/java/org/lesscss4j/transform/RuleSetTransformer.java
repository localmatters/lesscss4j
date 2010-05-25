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
package org.lesscss4j.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lesscss4j.model.RuleSet;

public class RuleSetTransformer extends AbstractDeclarationContainerTransformer<RuleSet> {
    public List<RuleSet> transform(RuleSet ruleSet, EvaluationContext context) {
        RuleSet transformed = ruleSet.clone();
        
        // Rule sets with arguments shouldn't be processed since they serve
        // only as a template for use by other rule sets in the stylesheet.
        if (ruleSet.getArguments().size() > 0) {
            return Arrays.asList(ruleSet);
        }

        transformed.clearDeclarations();
        List<RuleSet> ruleSetList = new ArrayList<RuleSet>();
        ruleSetList.add(transformed);
        doTransform(ruleSet, ruleSetList, context);
        return ruleSetList;
    }
}