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

public class StyleSheetTransformer extends AbstractTransformer<StyleSheet> {
    private Transformer<Page> _pageTransformer;
    private Transformer<Media> _mediaTransformer;
    private Transformer<RuleSet> _ruleSetTransformer;

    public Transformer<Page> getPageTransformer() {
        return _pageTransformer;
    }

    public void setPageTransformer(Transformer<Page> pageTransformer) {
        _pageTransformer = pageTransformer;
    }

    public Transformer<Media> getMediaTransformer() {
        return _mediaTransformer;
    }

    public void setMediaTransformer(Transformer<Media> mediaTransformer) {
        _mediaTransformer = mediaTransformer;
    }

    public Transformer<RuleSet> getRuleSetTransformer() {
        return _ruleSetTransformer;
    }

    public void setRuleSetTransformer(Transformer<RuleSet> ruleSetTransformer) {
        _ruleSetTransformer = ruleSetTransformer;
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
            return getPageTransformer().transform((Page) element, styleContext);
        }
        else if (element instanceof Media) {
            return getMediaTransformer().transform((Media) element, styleContext);
        }
        else if (element instanceof RuleSet) {
            return getRuleSetTransformer().transform((RuleSet) element, styleContext);
        }
        else {
            // todo: error
            return null;
        }
    }

    public static Transformer<StyleSheet> createDefaultTransformer() {
        DeclarationTransformer declarationTransformer = new DeclarationTransformer();

        RuleSetTransformer ruleSetTransformer = new RuleSetTransformer();
        ruleSetTransformer.setDeclarationTransformer(declarationTransformer);
        ruleSetTransformer.setRuleSetTransformer(ruleSetTransformer);

        PageTransformer pageTransformer = new PageTransformer();
        pageTransformer.setDeclarationTransformer(declarationTransformer);
        pageTransformer.setRuleSetTransformer(ruleSetTransformer);

        MediaTransformer mediaTransformer = new MediaTransformer();
        mediaTransformer.setRuleSetTransformer(ruleSetTransformer);

        StyleSheetTransformer styleSheetTransformer = new StyleSheetTransformer();
        styleSheetTransformer.setMediaTransformer(mediaTransformer);
        styleSheetTransformer.setPageTransformer(pageTransformer);
        styleSheetTransformer.setRuleSetTransformer(ruleSetTransformer);

        return styleSheetTransformer;
    }
}
