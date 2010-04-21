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

import org.antlr.runtime.tree.Tree;
import org.lesscss4j.parser.Css21Parser;

public abstract class AbstractObjectFactory<T> implements ObjectFactory<T> {
    protected String formatNode(String prefix, Tree node) {
        return String.format("%s [%d=%s] %s",
                             prefix, node.getType(), Css21Parser.tokenNames[node.getType()], node.toString());
    }

    protected void handleUnexpectedChild(String prefix, Tree child) {
        throw new IllegalStateException(formatNode(prefix, child));
    }
}
