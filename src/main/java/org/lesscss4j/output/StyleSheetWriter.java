/**
 * File: StyleSheetWriter.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 12:23:09 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.output;

import java.io.IOException;
import java.io.OutputStream;

import org.lesscss4j.error.ErrorHandler;
import org.lesscss4j.model.StyleSheet;

public interface StyleSheetWriter {
    void write(OutputStream output, StyleSheet styleSheet, ErrorHandler errorHandler) throws IOException;
}
