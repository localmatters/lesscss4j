/**
 * File: AbstractLessCssCompilerTest.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 19, 2010
 * Creation Time: 10:14:08 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.localmatters.lesscss4j.compile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.localmatters.lesscss4j.error.ErrorHandler;
import org.localmatters.lesscss4j.output.PrettyPrintOptions;
import org.localmatters.lesscss4j.parser.UrlStyleSheetResource;
import org.localmatters.lesscss4j.spring.LessCssCompilerFactoryBean;

public abstract class AbstractLessCssCompilerTest extends TestCase {
    public static final String ENCODING = "UTF-8";

    protected LessCssCompiler _compiler;
    protected PrettyPrintOptions _printOptions;
    protected ErrorHandler _errorHandler;

    @Override
    protected void setUp() throws Exception {
        _printOptions = new PrettyPrintOptions();
        _printOptions.setSingleDeclarationOnOneLine(true);
        _printOptions.setLineBetweenRuleSets(false);
        _printOptions.setOpeningBraceOnNewLine(false);
        _printOptions.setIndentSize(2);

        LessCssCompilerFactoryBean factoryBean = new LessCssCompilerFactoryBean();
        factoryBean.setDefaultEncoding(ENCODING);
        factoryBean.setPrettyPrintEnabled(true);
        factoryBean.setPrettyPrintOptions(_printOptions);
        factoryBean.afterPropertiesSet();
        _compiler = (LessCssCompiler) factoryBean.getObject();
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
        compileAndValidate(lessFile, cssFile, null);
    }

    protected void compileAndValidate(String lessFile, String cssFile, Comparator<String> comparator) throws IOException {
        URL url = getClass().getClassLoader().getResource(lessFile);
        assertNotNull("Unable to open " + lessFile, url);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        _compiler.compile(new UrlStyleSheetResource(url), output, _errorHandler);

        output.close();

        if (_errorHandler == null || _errorHandler.getErrorCount() == 0) {
            String expected = readCss(cssFile);
            String actual = output.toString(ENCODING);
            if (comparator == null) {
                assertEquals(expected, actual);
            }
            else {
                comparator.compare(expected, actual);
            }
        }
    }
}
