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
package org.lesscss4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface LessCssCompiler {
    void compile(InputStream input, OutputStream output) throws IOException;
}
