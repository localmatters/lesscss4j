/*
   Copyright 2010-present Local Matters, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
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
