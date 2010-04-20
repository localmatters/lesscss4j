/**
 * File: DefaultLessCssCompilerTest.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 20, 2010
 * Creation Time: 1:17:39 PM
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

import junit.framework.TestCase;

public class DefaultLessCssCompilerTest extends TestCase {
    DefaultLessCssCompiler _compiler;

    @Override
    protected void setUp() throws Exception {
        _compiler = new DefaultLessCssCompiler();
    }

    public void testParseCharset() throws IOException {
        assertEquals("UTF-8", _compiler.parseCharset("        @charset    'UTF-8'  ;  "));
        assertEquals("ISO-8859-1", _compiler.parseCharset("@charset \"ISO-8859-1\";  "));
    }

    public void testParseCharsetWithComments() throws IOException {
        assertEquals("UTF-8", _compiler.parseCharset("//@charset 'ISO-8859-1'\n" +
                                                     "/**\r" +
                                                     "  @charset \"ASCII\"\n" +
                                                     "*\n" +
                                                     "/* */  /* another */  \n" +
                                                     "    @charset    'UTF-8'  ;  "));
    }
    
    public void testParseCharsetNoCharset() throws IOException {
        assertEquals(null, _compiler.parseCharset("   @import url(something)"));
        assertEquals(null, _compiler.parseCharset("//\n comment" +
                                                  "   /* another comment\n" +
                                                  "  */   \r" +
                                                  "   .classname { }"));
        assertEquals(null, _compiler.parseCharset("// comment\n" +
                                                  "   /* another comment\n" +
                                                  "  */   \r" +
                                                  "   "));
        assertEquals(null, _compiler.parseCharset(" "));
        assertEquals(null, _compiler.parseCharset(""));
    }
}
