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
import org.lesscss4j.model.AbstractElement;
import org.lesscss4j.model.Media;
import org.lesscss4j.model.Page;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.StyleSheet;
import org.lesscss4j.model.expression.Expression;

import static org.lesscss4j.parser.antlr.LessCssLexer.*;

public class StyleSheetFactory extends AbstractObjectFactory<StyleSheet> {
    private ObjectFactory<RuleSet> _ruleSetFactory;
    private ObjectFactory<Media> _mediaFactory;
    private ObjectFactory<Page> _pageFactory;
    private ObjectFactory<Expression> _expressionFactory;

    public ObjectFactory<Expression> getExpressionFactory() {
        return _expressionFactory;
    }

    public void setExpressionFactory(ObjectFactory<Expression> expressionFactory) {
        _expressionFactory = expressionFactory;
    }

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
        if (styleSheetNode == null) {
            return stylesheet;
        }

        if (styleSheetNode.getType() == RULESET) {
            // Special case for when there's only one ruleset
            processStyleSheetNode(stylesheet, styleSheetNode);
        }
        else {
            for (int idx = 0, numChildren = styleSheetNode.getChildCount(); idx < numChildren; idx++) {
                Tree child = styleSheetNode.getChild(idx);
                processStyleSheetNode(stylesheet, child);
            }
        }

        return stylesheet;
    }

    private void processStyleSheetNode(StyleSheet stylesheet, Tree child) {
        switch (child.getType()) {
            case CHARSET:
                String charset = child.getChild(0).getText();
                charset = charset.replaceFirst("['\"]\\s*(\\S*)\\s*['\"]", "$1");
                if (charset.length() > 0) {
                    stylesheet.setCharset(charset);
                }
                break;

            case IMPORT:
                stylesheet.addImport(child.getChild(0).getText());
                break;

            case VAR:
                Tree exprNode = child.getChild(1);
                Expression expr = getExpressionFactory().create(exprNode);
                if (expr != null) {
                    stylesheet.setVariable(child.getChild(0).getText(), expr);
                }
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

    public static ObjectFactory<StyleSheet> createDefaultObjectFactory() {
        ExpressionFactory expressionFactory = new ExpressionFactory();

        DeclarationFactory declarationFactory = new DeclarationFactory();
        declarationFactory.setExpressionFactory(expressionFactory);

        RuleSetFactory ruleSetFactory = new RuleSetFactory();
        ruleSetFactory.setSelectorFactory(new SelectorFactory());
        ruleSetFactory.setDeclarationFactory(declarationFactory);
        ruleSetFactory.setExpressionFactory(expressionFactory);

        MediaFactory mediaFactory = new MediaFactory();
        mediaFactory.setRuleSetFactory(ruleSetFactory);
        mediaFactory.setExpressionFactory(expressionFactory);

        PageFactory pageFactory = new PageFactory();
        pageFactory.setDeclarationFactory(declarationFactory);
        pageFactory.setExpressionFactory(expressionFactory);

        StyleSheetFactory styleSheetFactory = new StyleSheetFactory();
        styleSheetFactory.setRuleSetFactory(ruleSetFactory);
        styleSheetFactory.setMediaFactory(mediaFactory);
        styleSheetFactory.setPageFactory(pageFactory);
        styleSheetFactory.setExpressionFactory(expressionFactory);

        return styleSheetFactory;
    }

}
