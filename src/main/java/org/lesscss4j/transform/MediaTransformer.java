/**
 * File: MediaTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:35:31 AM
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

import java.util.Arrays;
import java.util.List;

import org.lesscss4j.model.BodyElement;
import org.lesscss4j.model.Media;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.transform.manager.TransformerManager;

public class MediaTransformer extends AbstractTransformer<Media> {
    public MediaTransformer(TransformerManager transformerManager) {
        super(transformerManager);
    }

    public MediaTransformer() {
    }

    public List<Media> transform(Media media, EvaluationContext context) {
        Media transformed = new Media(media, false);
        evaluateVariables(media, transformed, context);
        transformBodyElements(media, transformed, context);
        return Arrays.asList(transformed);
    }

    protected void transformBodyElements(Media media, Media transformed, EvaluationContext context) {
        EvaluationContext mediaContext = new EvaluationContext();
        mediaContext.setParentContext(context);
        mediaContext.setVariableContainer(transformed);
        mediaContext.setRuleSetContainer(transformed);

        for (BodyElement element : media.getBodyElements()) {
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
