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
import java.net.URL;
import java.util.Comparator;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.lesscss4j.output.PrettyPrintOptions;
import org.lesscss4j.parser.UrlStyleSheetResource;
import org.lesscss4j.spring.LessCssCompilerFactoryBean;

public class LessCssCompilerImplTest extends TestCase {
    public static final String ENCODING = "UTF-8";

    LessCssCompiler _compiler;
    PrettyPrintOptions _printOptions;

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

        _compiler.compile(new UrlStyleSheetResource(url), output);

        output.close();

        String expected = readCss(cssFile);
        String actual = output.toString(ENCODING);
        if (comparator == null) {
            assertEquals(expected, actual);
        }
        else {
            comparator.compare(expected, actual);
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

    public void testNestedRuleSets() throws IOException {
        compileAndValidate("less/rulesets.less", "css/rulesets.css");
    }

    public void testMixinVariableScope() throws IOException {
        compileAndValidate("less/scope.less", "css/scope.css");
    }

    public void testMixinArgs() throws IOException {
        compileAndValidate("less/mixins-args.less", "css/mixins-args.css");
    }
    
    public void testMultipleSelectors() throws IOException {
        compileAndValidate("less/selectors.less", "css/selectors.css");
    }
    
    public void testCss3SingleRun() throws IOException {
        compileAndValidate("less/singlerun.less", "css/singlerun.css");
    }

    public void testColorMath() throws IOException {
        compileAndValidate("less/colors.less", "css/colors.css");
    }

    public void testImport() throws IOException {
        compileAndValidate("less/import.less", "css/import.css");
    }

    public void testDashPrefix() throws IOException {
        compileAndValidate("less/dash-prefix.less", "css/dash-prefix.css");
    }

    public void testInternetExplorer() throws IOException {
        compileAndValidate("less/ie.less", "css/ie.css");
    }

    public void testBigCssFile() throws IOException {
        _printOptions.setSingleDeclarationOnOneLine(false);
        compileAndValidate("less/css-big.less", "css/css-big.css");
    }

    public void testBigCssFileCompareToSelf()  throws IOException {
        compileAndValidate("css/big.css", "css/big.css", new Comparator<String>() {
            public int compare(String expected, String actual) {
                assertEquals(expected.toLowerCase(), actual.toLowerCase());
                return 0;
            }
        });
    }
}