/**
 * File: StyleSheetTreeParser.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 18, 2010
 * Creation Time: 9:57:46 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.parser;

import java.io.IOException;

import org.antlr.runtime.tree.Tree;
import org.lesscss4j.error.ErrorHandler;

/**
 * Parses the given input into an AST tree.
 */
public interface StyleSheetTreeParser {
    Tree parseTree(StyleSheetResource input, ErrorHandler handler) throws IOException;
}
