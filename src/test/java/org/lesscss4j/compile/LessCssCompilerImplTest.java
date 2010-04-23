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
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.lesscss4j.output.PrettyPrintOptions;
import org.lesscss4j.output.StyleSheetWriterImpl;
import org.lesscss4j.parser.LessCssStyleSheetParser;

public class LessCssCompilerImplTest extends TestCase {
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

    protected String readCss(String cssFile) throws IOException {
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(cssFile);
            assertNotNull("Unable to open " + cssFile, input);
            return IOUtils.toString(input, ENCODING);
        }
        finally {
            IOUtils.closeQuietly(input);
        }
    }

    protected void compileAndValidate(String lessFile, String cssFile) throws IOException {
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(lessFile);
            assertNotNull("Unable to open " + lessFile, input);

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            _compiler.compile(input, output);

            assertEquals(readCss(cssFile), output.toString(ENCODING));
        }
        finally {
            try {
                if (input != null) input.close();
            }
            catch (IOException ex) { /* do nothing */ }
        }

    }

    public void testVariables() throws IOException {
        compileAndValidate("less/variables.less", "css/variables.css");
    }
}