/**
 * File: LessCssStyleSheetParser.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 1:01:01 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.lesscss4j.error.ParseError;
import org.lesscss4j.error.ParseException;
import org.lesscss4j.factory.ObjectFactory;
import org.lesscss4j.factory.StyleSheetFactory;
import org.lesscss4j.model.StyleSheet;
import org.lesscss4j.parser.antlr.LessCssLexer;
import org.lesscss4j.parser.antlr.LessCssParser;

public class LessCssStyleSheetParser implements StyleSheetParser, StyleSheetTreeParser {
    private String _defaultEncoding;
    private int _initialBufferSize = ANTLRInputStream.INITIAL_BUFFER_SIZE;
    private int _readBufferSize = ANTLRInputStream.READ_BUFFER_SIZE;
    private ObjectFactory<StyleSheet> _styleSheetFactory;

    public static final String CHARSET_SYM = "@charset";
    public static final String NEWLINE_CHARS = "\n\r\f";
    
    public String getDefaultEncoding() {
        return _defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        _defaultEncoding = defaultEncoding;
    }

    public int getInitialBufferSize() {
        return _initialBufferSize;
    }

    public void setInitialBufferSize(int initialBufferSize) {
        _initialBufferSize = initialBufferSize;
    }

    public int getReadBufferSize() {
        return _readBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        _readBufferSize = readBufferSize;
    }

    public ObjectFactory<StyleSheet> getStyleSheetFactory() {
        if (_styleSheetFactory == null) {
            _styleSheetFactory = createDefaultStyleSheetFactory();
        }
        return _styleSheetFactory;
    }

    public void setStyleSheetFactory(ObjectFactory<StyleSheet> styleSheetFactory) {
        _styleSheetFactory = styleSheetFactory;
    }

    protected ObjectFactory<StyleSheet> createDefaultStyleSheetFactory() {
        StyleSheetFactory styleSheetObjectFactory = (StyleSheetFactory) StyleSheetFactory.createDefaultObjectFactory();
        styleSheetObjectFactory.setStyleSheetTreeParser(this);
        return styleSheetObjectFactory;
    }

    public StyleSheet parse(StyleSheetResource input) throws IOException {
        return getStyleSheetFactory().create(new StyleSheetTree(parseTree(input), input));

    }

    public Tree parseTree(StyleSheetResource input) throws IOException {
        LessCssLexer lexer = new LessCssLexer(createANTLRInputStream(input.getInputStream()));
        LessCssParser parser = new LessCssParser(new CommonTokenStream(lexer));
        try {
            LessCssParser.styleSheet_return result = parser.styleSheet();
            if (!parser.getErrors().isEmpty()) {
                throw new ParseException(parser.getErrors());
            }
            return (Tree) result.getTree();
        }
        catch (RecognitionException e) {
            throw new ParseError(e, parser.getErrorHeader(e), parser.getErrorMessage(e, parser.getTokenNames()));
        }
    }

    protected ANTLRInputStream createANTLRInputStream(InputStream input) throws IOException {
        String encoding = null;

        // Read a buffer of data to see if we can find the @charset symbol in the beginning of the file.
        // Otherwise just use the default encoding.
        PushbackInputStream pushbackStream = new PushbackInputStream(input, getReadBufferSize());
        byte[] buf = new byte[getReadBufferSize()];
        int len = pushbackStream.read(buf, 0, buf.length);
        if (len >= 0) {
            pushbackStream.unread(buf, 0, len);
            String bufStr = new String(buf, 0, len, "ASCII");
            encoding = parseCharset(bufStr);
        }

        if (encoding == null) {
            encoding = getDefaultEncoding();
        }

        return new ANTLRInputStream(pushbackStream, getInitialBufferSize(), getReadBufferSize(), encoding);
    }

    /**
     * Attempt to find a @charset directive in the given string buffer.
     * @param buffer The buffer to parse
     * @return The parsed charset name
     * @throws IOException
     */
    protected String parseCharset(String buffer) throws IOException {
        int idx = 0;
        boolean comment = false;
        while (idx < buffer.length()) {
            if (comment) {
                // block comment contents
                if (buffer.regionMatches(idx, "*/", 0, 2)) {
                    idx += 2;
                    comment = false;
                }
                else {
                    idx++;
                }
            }
            else if (buffer.regionMatches(idx, "//", 0, 2)) {
                // line comment
                idx += 2;
                while (idx < buffer.length() && NEWLINE_CHARS.indexOf(buffer.charAt(idx)) < 0) {
                    idx++;
                }
            }
            else if (buffer.regionMatches(idx, "/*", 0, 2)) {
                // Start of block comment
                idx += 2;
                comment = true;
            }
            else if (Character.isWhitespace(buffer.charAt(idx))) {
                idx++;
            }
            else if (buffer.regionMatches(idx, CHARSET_SYM, 0, CHARSET_SYM.length())) {
                // Charset symbol
                idx += CHARSET_SYM.length();
                while (Character.isWhitespace(buffer.charAt(idx))) {
                    idx++;
                }

                // We should be at either a single quote or double quote
                char quoteChar = buffer.charAt(idx++);
                if (quoteChar != '\'' && quoteChar != '"') {
                    throw new IOException("Invalid " + CHARSET_SYM + " specification");
                }

                // Find the closing quote
                int startIdx = idx;
                while (idx < buffer.length() &&
                       NEWLINE_CHARS.indexOf(buffer.charAt(idx)) < 0 &&
                       buffer.charAt(idx) != quoteChar) {

                    idx++;
                }

                if (idx >= buffer.length()) {
                    throw new IOException("Unbalanced quote in " + CHARSET_SYM);
                }

                return buffer.substring(startIdx, idx);
            }
            else {
                // non whitespace.  @charset must be first thing in the file, so we can stop looking for one now.
                return null;
            }
        }
        return null;
    }
}
