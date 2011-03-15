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
import java.util.Comparator;

public class LessCssCompilerTest extends AbstractLessCssCompilerTest {

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

    public void testMediaAndPage() throws IOException {
        compileAndValidate("less/media-page.less", "css/media-page.css");
    }

    public void testAccessors() throws IOException {
        compileAndValidate("less/accessors.less", "css/accessors.css");
    }
    
    public void testFunctions() throws IOException {
        compileAndValidate("less/functions.less", "css/functions.css");
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