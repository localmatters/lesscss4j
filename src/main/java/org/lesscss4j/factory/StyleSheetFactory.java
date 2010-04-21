/**
 * File: StyleSheetFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:27:01 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.factory;

import org.antlr.runtime.tree.Tree;
import org.lesscss4j.model.Media;
import org.lesscss4j.model.Page;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.StyleSheet;

import static org.lesscss4j.parser.Css21Lexer.*;

public class StyleSheetFactory extends AbstractObjectFactory<StyleSheet> {
    private ObjectFactory<RuleSet> _ruleSetFactory;
    private ObjectFactory<Media> _mediaFactory;
    private ObjectFactory<Page> _pageFactory;

    public ObjectFactory<RuleSet> getRuleSetFactory() {
        return _ruleSetFactory;
    }

    public void setRuleSetFactory(ObjectFactory<RuleSet> ruleSetFactory) {
        _ruleSetFactory = ruleSetFactory;
    }

    public ObjectFactory<Media> getMediaFactory() {
        return _mediaFactory;
    }

    public void setMediaFactory(ObjectFactory<Media> mediaFactory) {
        _mediaFactory = mediaFactory;
    }

    public ObjectFactory<Page> getPageFactory() {
        return _pageFactory;
    }

    public void setPageFactory(ObjectFactory<Page> pageFactory) {
        _pageFactory = pageFactory;
    }

    public StyleSheet create(Tree styleSheetNode) {
        StyleSheet stylesheet = new StyleSheet();
        for (int idx = 0, numChildren = styleSheetNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = styleSheetNode.getChild(idx);
            switch (child.getType()) {
                case CHARSET:
                    String charset = child.getChild(0).getText();
                    charset = charset.replaceFirst("['\"]\\s*(\\S*)\\s*['\"]", "$1");
                    if (charset.length() > 0) {
                        stylesheet.setCharset(charset);
                    }
                    break;

                case IMPORT:
                    // todo
                    break;

                case RULESET:
                    RuleSet ruleSet = getRuleSetFactory().create(child);
                    if (ruleSet != null) {
                        stylesheet.addBodyElement(ruleSet);
                    }
                    break;

                case MEDIA_SYM:
                    Media media = getMediaFactory().create(child);
                    if (media != null) {
                        stylesheet.addBodyElement(media);
                    }
                    break;

                case PAGE_SYM:
                    Page page = getPageFactory().create(child);
                    if (page != null) {
                        stylesheet.addBodyElement(page);
                    }
                    break;

                default:
                    handleUnexpectedChild("Unexpected stylesheet child:", child);
                    break;
            }
        }
        return stylesheet;
    }
}
