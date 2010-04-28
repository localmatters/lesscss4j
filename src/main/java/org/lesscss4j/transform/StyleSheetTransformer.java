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

    public void transform(StyleSheet styleSheet, EvaluationContext context) {
        // todo: process imports

        evaluateVariables(styleSheet, context);

        transformBodyElements(styleSheet, context);
    }

    protected void transformBodyElements(StyleSheet styleSheet, EvaluationContext context) {
        EvaluationContext styleContext = new EvaluationContext();
        styleContext.setParentContext(context);
        styleContext.setVariableContainer(styleSheet);
        styleContext.setRuleSetContainer(styleSheet);

        List<BodyElement> elements = styleSheet.getBodyElements();
        for (int idx = 0; idx < elements.size(); idx++) {
            BodyElement element = elements.get(idx);
            styleContext.setRuleSetIndex(idx);
            if (element instanceof Page) {
                getPageTransformer().transform((Page) element, styleContext);
            }
            else if (element instanceof Media) {
                getMediaTransformer().transform((Media) element, styleContext);
            }
            else if (element instanceof RuleSet) {
                getRuleSetTransformer().transform((RuleSet) element, styleContext);
            }

            if (styleContext.getRuleSetIndex() > idx) {
                idx = styleContext.getRuleSetIndex();
            }
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
