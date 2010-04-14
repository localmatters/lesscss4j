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

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;
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

    public void processFile(String filename) throws Exception {
        Css21Lexer lexer = new Css21Lexer(new ANTLRFileStream(filename, "UTF-8"));
        Css21Parser parser = new Css21Parser(new CommonTokenStream(lexer));
        Css21Parser.styleSheet_return result = parser.styleSheet();

        System.out.println(treeToString((Tree)result.getTree()));
//        System.out.println(((CommonTree) result.getTree()).toStringTree());
/*
        DOTTreeGenerator gen = new DOTTreeGenerator();
        StringTemplate st = gen.toDOT(((Tree) result.getTree()));
        System.out.println(st);
*/

    }
    protected String treeToString(Tree tree) {
        StringBuilder builder = new StringBuilder();
        treeToString(tree, builder, 0);
        return builder.toString();
    }

    protected void treeToString(Tree tree, StringBuilder builder, int level) {
        for (int idx = 0; idx < level*2; idx++) {
            builder.append(' ');
        }
        builder.append(tree.toString());
        builder.append('\n');

        for (int idx = 0, numChildren = tree.getChildCount(); idx < numChildren; idx++) {
            treeToString(tree.getChild(idx), builder, level + 1);
        }
    }
}
