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

package org.localmatters.lesscss4j.cli;

import junit.framework.TestCase;

public class CompilerMainTest extends TestCase {
    CompilerMain _main;

    @Override
    protected void setUp() throws Exception {
        _main = new CompilerMain();
    }

    public void testGenerateOutputFilename() {
        assertEquals("test.css", _main.generateOutputFilename("test.less"));
        assertEquals("test.css", _main.generateOutputFilename("test.txt"));
        assertEquals("test.css", _main.generateOutputFilename("test."));
        assertEquals("test.css", _main.generateOutputFilename("test"));
        assertEquals("test-min.css", _main.generateOutputFilename("test.css"));
    }

}
