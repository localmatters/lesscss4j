/**
 * File: StyleSheetTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:19:42 AM
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
import org.lesscss4j.model.Page;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.StyleSheet;
import org.lesscss4j.transform.manager.TransformerManager;

public class StyleSheetTransformer extends AbstractTransformer<StyleSheet> {
    public StyleSheetTransformer() {
    }

    public StyleSheetTransformer(TransformerManager transformerManager) {
        super(transformerManager);
    }

    public List<StyleSheet> transform(StyleSheet styleSheet, EvaluationContext context) {
        StyleSheet transformed = new StyleSheet();

        processImports(styleSheet, transformed, context);
        evaluateVariables(styleSheet, transformed, context);

        transformBodyElements(styleSheet, transformed, context);

        return Arrays.asList(transformed);
    }

    protected void processImports(StyleSheet styleSheet, StyleSheet transformed, EvaluationContext context) {
        // Imports are handled by the Parser.  Don't need to do anything since
        // we don't want to output any @import statements in the writer.
    }

    protected void transformBodyElements(StyleSheet styleSheet, StyleSheet transformed, EvaluationContext context) {
        EvaluationContext styleContext = new EvaluationContext();
        styleContext.setParentContext(context);
        styleContext.setVariableContainer(transformed);
        styleContext.setRuleSetContainer(transformed);

        List<BodyElement> elements = styleSheet.getBodyElements();
        for (BodyElement element : elements) {
            List<? extends BodyElement> transformedElementList = transformBodyElement(element, styleContext);
            if (transformedElementList != null) {
                for (BodyElement transformedElement : transformedElementList) {
                    transformed.addBodyElement(transformedElement);
                }
            }
        }
    }

    private List<? extends BodyElement> transformBodyElement(BodyElement element, EvaluationContext styleContext) {
        if (element instanceof Page) {
            Page page = (Page) element;
            return getTransformer(page).transform(page, styleContext);
        }
        else if (element instanceof Media) {
            Media media = (Media) element;
            return getTransformer(media).transform(media, styleContext);
        }
        else if (element instanceof RuleSet) {
            RuleSet ruleSet = (RuleSet) element;
            return getTransformer(ruleSet).transform(ruleSet, styleContext);
        }
        else {
            // todo: error
            return null;
        }
    }
}
