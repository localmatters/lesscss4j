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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;

import org.antlr.runtime.tree.Tree;
import org.lesscss4j.output.StyleSheetWriterImpl;
import org.lesscss4j.parser.Css21Parser;

import static org.lesscss4j.parser.LessCssLexer.*;

public class Test {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java " + Test.class.getName() + " <filename>");
            System.exit(-1);
        }
        new Test().processFile(args[0]);
    }

    public void processFile(String filename) throws Exception {
        System.out.println("Reading file:" + filename);
        LessCssCompilerImpl compiler = new LessCssCompilerImpl();
        ((StyleSheetWriterImpl) compiler.getStyleSheetWriter()).setPrettyPrintEnabled(true);
        compiler.compile(new FileInputStream(filename), System.out);
/*
        LessCssLexer lexer = new LessCssLexer(new ANTLRFileStream(filename, "UTF-8"));
        LessCssParser parser = new LessCssParser(new CommonTokenStream(lexer));
        LessCssParser.styleSheet_return result = parser.styleSheet();

        StringWriter writer = new StringWriter();
        Tree tree = (Tree) result.getTree();

        processStylesheet(tree, writer);

        System.out.println(writer.toString());
*/
//        System.out.println(treeToString((Tree)result.getTree()));
//        System.out.println(((CommonTree) result.getTree()).toStringTree());
/*
        DOTTreeGenerator gen = new DOTTreeGenerator();
        StringTemplate st = gen.toDOT(((Tree) result.getTree()));
        System.out.println(st);
*/

    }

    protected String formatNode(String prefix, Tree node) {
        return String.format("%s [%d=%s] %s",
                             prefix, node.getType(), Css21Parser.tokenNames[node.getType()], node.toString());
    }

    protected void processStylesheet(Tree tree, Writer writer) throws IOException {
        for (int idx = 0, numChildren = tree.getChildCount(); idx < numChildren; idx++) {
            Tree child = tree.getChild(idx);
            switch (child.getType()) {
                case CHARSET:
                    processCharset(child, writer);
                    break;
                case IMPORT:
                    processImport(child, writer);
                    break;
                case RULESET:
                    processRuleset(child, writer);
                    break;
                case MEDIA_SYM:
                    writer.write("/* TODO: @media */");
                    break;
                case WS:
                    break;
                case EOF:
                    break;
                default:
                    throw new IllegalStateException(formatNode("Unexpected stylesheet child:", child));
            }
        }
    }

    protected void processCharset(Tree tree, Writer writer) throws IOException {
        writer.write("@charset ");
        writer.write(tree.getChild(0).toString());
        writer.write(";");
    }

    protected void processImport(Tree tree, Writer writer) throws IOException {
        writer.write("@import ");
        writer.write(tree.getChild(0).toString());
        writer.write(";");
    }

    protected void processRuleset(Tree tree, Writer writer) throws IOException {
        int selectorCount = 0;
        int declarationCount = 0;
        // todo: preprocess to find all the declarations....count of 0 == suppress
        for (int idx = 0, numChildren = tree.getChildCount(); idx < numChildren; idx++) {
            Tree child = tree.getChild(idx);
            switch (child.getType()) {
                case SELECTOR:
                    if (selectorCount > 0) {
                        writer.write(",");
                    }
                    processSelector(child, writer);
                    selectorCount++;
                    break;
                case DECLARATION:
                    if (declarationCount == 0) {
                        writer.write("{");
                    }
                    processDeclaration(child, writer);
                    declarationCount++;
                    break;
                case WS:
                    break;
                default:
                    throw new IllegalStateException(formatNode("Unexpected ruleset child:", child));
            }
        }
        if (declarationCount > 0) {
            writer.write("}");
        }
    }

    protected void processDeclaration(Tree tree, Writer writer) throws IOException {
        Tree property = tree.getChild(0);
        Tree propValue = property.getChild(0);
        if (propValue.getType() == STAR) {
            writer.write('*');
            propValue = property.getChild(1);
        }
        writer.write(property.toString());
        writer.write(":");
        for (int idx = 0, numChildren = propValue.getChildCount(); idx < numChildren; idx++) {
            Tree child = propValue.getChild(idx);
            if (child.getType() == WS) {
                writer.write(" ");
            }
            else {
                writer.write(child.toString());
            }
        }
        writer.write(";");
    }

    protected void processSelector(Tree tree, Writer writer) throws IOException {
        for (int idx = 0, numChildren = tree.getChildCount(); idx < numChildren; idx++) {
            Tree child = tree.getChild(idx);
            if (child.getType() == WS) {
                writer.write(" ");
            }
            else {
                writer.write(child.toString());
            }
        }
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
