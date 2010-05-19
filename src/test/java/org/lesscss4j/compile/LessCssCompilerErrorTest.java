/**
 * File: StandardCss21Test.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 7:50:55 AM
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
import java.io.PrintWriter;
import java.io.StringWriter;

import org.lesscss4j.error.WriterErrorHandler;

public class LessCssCompilerErrorTest extends AbstractLessCssCompilerTest {
    StringWriter _writer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        _writer = new StringWriter();

        _errorHandler = new WriterErrorHandler();
        ((WriterErrorHandler) _errorHandler).setLogStackTrace(false);
        ((WriterErrorHandler) _errorHandler).setWriter(new PrintWriter(_writer));
    }

    public void testMismatchedUnits() throws IOException {
        compileAndValidate("less/exceptions/mixed-units-error.less", null);
        assertEquals(3, _errorHandler.getErrorCount());
        String errors = _writer.toString();
        assertEquals("[1:8] - Unit mismatch: 1px 1%\n" +
                     "[5:17] - Unit mismatch: #000 1em\n" +
                     "[3:13] - Unit mismatch: 1px #fff\n",
                     errors);
    }
}