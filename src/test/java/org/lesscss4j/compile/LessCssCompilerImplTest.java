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

            output.close();

            assertEquals(readCss(cssFile), output.toString(ENCODING));
        }
        finally {
            IOUtils.closeQuietly(input);
        }

    }

    public void testVariables() throws IOException {
        compileAndValidate("less/variables.less", "css/variables.css");
    }

    public void testLazyEvalVariables() throws IOException {
        compileAndValidate("less/lazy-eval.less", "css/lazy-eval.css");
    }

    public void testPlainCss() throws IOException {
        compileAndValidate("less/css.less", "css/css.css");
    }

    public void testComments() throws IOException {
        compileAndValidate("less/comments.less", "css/comments.css");
    }

    public void testCss3() throws IOException {
        compileAndValidate("less/css-3.less", "css/css-3.css");
    }

    public void testExpressionParens() throws IOException {
        compileAndValidate("less/parens.less", "css/parens.css");
    }

    public void testOperations() throws IOException {
        compileAndValidate("less/operations.less", "css/operations.css");
    }

    public void testStrings() throws IOException {
        compileAndValidate("less/strings.less", "css/strings.css");
    }

    public void testMixins() throws IOException {
        compileAndValidate("less/mixins.less", "css/mixins.css");
    }

/*
    public void testNestedRuleSets() throws IOException {
        compileAndValidate("less/rulesets.less", "css/rulesets.css");
    }
*/

    public void testMixinVariableScope() throws IOException {
        compileAndValidate("less/scope.less", "css/scope.css");
    }
}