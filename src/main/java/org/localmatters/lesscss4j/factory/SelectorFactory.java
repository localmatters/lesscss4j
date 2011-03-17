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
package org.localmatters.lesscss4j.factory;

import org.antlr.runtime.tree.Tree;
import org.localmatters.lesscss4j.error.ErrorHandler;
import org.localmatters.lesscss4j.model.Selector;

import static org.localmatters.lesscss4j.parser.antlr.LessCssLexer.WS;

public class SelectorFactory extends AbstractObjectFactory<Selector> {
    public Selector create(Tree selectorNode, ErrorHandler errorHandler) {
        Selector selector = null;
        
        String selectorText = concatChildNodeText(selectorNode);
        if (selectorText.length() > 0) {
            selector = new Selector(selectorText.toString());
            selector.setLine(selectorNode.getLine());
            selector.setChar(selectorNode.getCharPositionInLine());
        }
        return selector; 
    }
}
