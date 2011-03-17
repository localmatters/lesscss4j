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
