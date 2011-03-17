/**
 * File: LessCssStyleSheetParserTest.java
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
package org.localmatters.lesscss4j.parser;

import java.io.IOException;

import junit.framework.TestCase;

public class LessCssStyleSheetParserTest extends TestCase {
    LessCssStyleSheetParser _parser;

    @Override
    protected void setUp() throws Exception {
        _parser = new LessCssStyleSheetParser();
    }

    public void testParseCharset() throws IOException {
        assertEquals("UTF-8", _parser.parseCharset("        @charset    'UTF-8'  ;  "));
        assertEquals("ISO-8859-1", _parser.parseCharset("@charset \"ISO-8859-1\";  "));
    }

    public void testParseCharsetWithComments() throws IOException {
        assertEquals("UTF-8", _parser.parseCharset("//@charset 'ISO-8859-1'\n" +
                                                     "/**\r" +
                                                     "  @charset \"ASCII\"\n" +
                                                     "*\n" +
                                                     "/* */  /* another */  \n" +
                                                     "    @charset    'UTF-8'  ;  "));
    }
    
    public void testParseCharsetNoCharset() throws IOException {
        assertEquals(null, _parser.parseCharset("   @import url(something)"));
        assertEquals(null, _parser.parseCharset("//\n comment" +
                                                  "   /* another comment\n" +
                                                  "  */   \r" +
                                                  "   .classname { }"));
        assertEquals(null, _parser.parseCharset("// comment\n" +
                                                  "   /* another comment\n" +
                                                  "  */   \r" +
                                                  "   "));
        assertEquals(null, _parser.parseCharset(" "));
        assertEquals(null, _parser.parseCharset(""));
    }
}
