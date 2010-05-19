/**
 * File: LessCssCompiler.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 20, 2010
 * Creation Time: 12:41:24 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.compile;

import java.io.IOException;
import java.io.OutputStream;

import org.lesscss4j.error.ErrorHandler;
import org.lesscss4j.parser.StyleSheetResource;

public interface LessCssCompiler {
    void compile(StyleSheetResource input, OutputStream output, ErrorHandler errorHandler) throws IOException;
}
