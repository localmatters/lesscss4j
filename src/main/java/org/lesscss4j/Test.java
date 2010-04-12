/**
 * File: Test.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 12, 2010
 * Creation Time: 10:26:14 AM
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

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.lesscss4j.parser.Css21Lexer;
import org.lesscss4j.parser.Css21Parser;

public class Test {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java " + Test.class.getName() + " <filename>");
            System.exit(-1);
        }
        new Test().processFile(args[0]);
    }

    public void processFile(String filename) throws IOException {
        Css21Lexer lexer = new Css21Lexer(new ANTLRFileStream(filename, "UTF-8"));
        Css21Parser parser = new Css21Parser(new CommonTokenStream(lexer));
    }
}
