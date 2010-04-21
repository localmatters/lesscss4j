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
import java.io.InputStream;

import org.lesscss4j.model.StyleSheet;

public interface StyleSheetParser {
    StyleSheet parse(InputStream input) throws IOException ;
}
