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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.lesscss4j.output.PrettyPrintOptions;
import org.lesscss4j.output.StyleSheetWriterImpl;
import org.lesscss4j.parser.LessCssStyleSheetParser;

public class StandardCss21Test extends TestCase {
    public static final String ENCODING = "UTF-8";

    LessCssCompilerImpl _compiler;

    @Override
    protected void setUp() throws Exception {
        PrettyPrintOptions printOptions = new PrettyPrintOptions();
        printOptions.setSingleDeclarationOnOneLine(true);
        printOptions.setLineBetweenRuleSets(false);
        printOptions.setOpeningBraceOnNewLine(false);
        printOptions.setIndentSize(2);

        _compiler = new LessCssCompilerImpl();

        ((LessCssStyleSheetParser) _compiler.getStyleSheetParser()).setDefaultEncoding(ENCODING);

        ((StyleSheetWriterImpl) _compiler.getStyleSheetWriter()).setPrettyPrintEnabled(true);
        ((StyleSheetWriterImpl) _compiler.getStyleSheetWriter()).setPrettyPrintOptions(printOptions);
        ((StyleSheetWriterImpl) _compiler.getStyleSheetWriter()).setDefaultEncoding(ENCODING);
    }

    public void testCssParseWrite() throws IOException {
        String resource = "css/big.css";
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(resource);
            assertNotNull("Unable to open " + resource, input);
            String original = IOUtils.toString(input, ENCODING);
            input.close();

            input = new ByteArrayInputStream(original.getBytes(ENCODING));
            assertNotNull("Unable to open " + resource, input);

            ByteArrayOutputStream output = new ByteArrayOutputStream(original.length());
            
            _compiler.compile(input, output);

            assertEquals(original.toLowerCase(), output.toString(ENCODING).toLowerCase());
        }
        finally {
            try {
                if (input != null) input.close();
            }
            catch (IOException ex) { /* do nothing */ }
        }
    }
}
