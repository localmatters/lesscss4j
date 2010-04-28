/**
 * File: MediaFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:34:25 AM
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
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.expression.Expression;

import static org.lesscss4j.parser.antlr.LessCssLexer.*;

public class MediaFactory extends AbstractObjectFactory<Media> {
    private ObjectFactory<Expression> _expressionFactory;
    private ObjectFactory<RuleSet> _ruleSetFactory;

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

    public Media create(Tree mediaNode) {
        Media media = new Media();
        media.setLine(mediaNode.getLine());
        media.setChar(mediaNode.getCharPositionInLine());
        for (int idx = 0, numChildren = mediaNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = mediaNode.getChild(idx);
            switch (child.getType()) {
                case IDENT:
                    media.addMedium(child.getText());
                    break;

                case RULESET:
                    RuleSet ruleSet = getRuleSetFactory().create(child);
                    if (ruleSet != null) {
                        media.addBodyElement(ruleSet);
                    }
                    break;

                case VAR:
                    Expression expr = getExpressionFactory().create(child.getChild(1));
                    if (expr != null) {
                        media.setVariable(child.getChild(0).getText(), expr);
                    }
                    break;

                default:
                    handleUnexpectedChild("Unexpected media child:", child);
                    break;
            }
        }

        // ignore empty media elements
        return media.getBodyElements() != null && media.getBodyElements().size() > 0 ? media : null;
    }
}
