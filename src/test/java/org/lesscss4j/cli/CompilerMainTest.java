/**
 * File: CompilerMainTest.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 5, 2010
 * Creation Time: 3:32:38 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.cli;

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
