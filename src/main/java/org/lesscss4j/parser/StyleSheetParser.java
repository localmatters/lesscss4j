/**
 * File: StyleSheetParser.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 1:00:20 PM
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

import org.lesscss4j.model.StyleSheet;

/**
 * Parses the given input into a StyleSheet object
 */
public interface StyleSheetParser {
    StyleSheet parse(StyleSheetResource input) throws IOException;
}
