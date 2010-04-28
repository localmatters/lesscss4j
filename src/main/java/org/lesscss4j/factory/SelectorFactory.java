/**
 * File: SelectorFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:44:14 AM
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
import org.lesscss4j.model.Selector;

import static org.lesscss4j.parser.antlr.LessCssLexer.WS;

public class SelectorFactory extends AbstractObjectFactory<Selector> {
    public Selector create(Tree selectorNode) {
        StringBuilder selectorText = new StringBuilder();
        for (int idx = 0, numChildren = selectorNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = selectorNode.getChild(idx);
            if (child.getType() == WS) {
                // Compress all whitespace into a single space
                selectorText.append(' ');
            }
            else {
                selectorText.append(child.getText());
            }
        }

        Selector selector = null;
        if (selectorText.length() > 0) {
            selector = new Selector(selectorText.toString());
            selector.setLine(selectorNode.getLine());
            selector.setChar(selectorNode.getCharPositionInLine());
        }
        return selector; 
    }
}
