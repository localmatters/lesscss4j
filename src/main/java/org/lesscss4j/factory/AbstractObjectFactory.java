/**
 * File: AbstractObjectFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 10:46:54 AM
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

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import org.lesscss4j.parser.antlr.LessCssParser;

import static org.lesscss4j.parser.antlr.LessCssLexer.*;

public abstract class AbstractObjectFactory<T> implements ObjectFactory<T> {
    protected String formatNode(String prefix, Tree node) {
        return String.format("%s [%d=%s] %d:%d - %s",
                             prefix, node.getType(), LessCssParser.tokenNames[node.getType()],  
                             node.getLine(), node.getCharPositionInLine(),
                             node.toString());
    }

    protected void handleUnexpectedChild(String prefix, Tree child) {
        int type = child.getType();
        if (type != WS && type != EOF) {
            throw new IllegalStateException(formatNode(prefix, child));
        }
    }

    protected String concatChildNodeText(Tree parent) {
        StringBuilder buf = new StringBuilder();
        for (int idx = 0, numChildren = parent.getChildCount(); idx < numChildren; idx++) {
            Tree child = parent.getChild(idx);
            buf.append(child.getText());
        }
        return buf.toString();
    }
}
