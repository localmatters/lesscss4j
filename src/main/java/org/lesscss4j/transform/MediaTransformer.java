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

import org.lesscss4j.model.BodyElement;
import org.lesscss4j.model.Media;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.expression.EvaluationContext;

public class MediaTransformer extends AbstractTransformer<Media> {
    private Transformer<RuleSet> _ruleSetTransformer;

    public Transformer<RuleSet> getRuleSetTransformer() {
        return _ruleSetTransformer;
    }

    public void setRuleSetTransformer(Transformer<RuleSet> ruleSetTransformer) {
        _ruleSetTransformer = ruleSetTransformer;
    }

    public void transform(Media media, EvaluationContext context) {
        evaluateVariables(media, context);
        transformBodyElements(media, context);
    }

    protected void transformBodyElements(Media media, EvaluationContext context) {
        EvaluationContext mediaContext = new EvaluationContext(media, context);
        for (BodyElement element : media.getBodyElements()) {
            if (element instanceof RuleSet) {
                getRuleSetTransformer().transform((RuleSet) element, mediaContext);
            }
            else {
                throw new IllegalStateException(
                    "Unexpected body element " + element.getClass().getSimpleName() + " in Media");
            }
        }
    }
}
