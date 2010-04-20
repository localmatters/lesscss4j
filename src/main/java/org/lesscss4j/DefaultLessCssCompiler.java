/**
 * File: DefaultLessCssCompiler.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 20, 2010
 * Creation Time: 12:44:39 PM
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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.io.IOExceptionWithCause;
import org.lesscss4j.parser.LessCssLexer;
import org.lesscss4j.parser.LessCssParser;

public class DefaultLessCssCompiler implements LessCssCompiler {
    private String _defaultEncoding;
    private int _initialBufferSize = ANTLRInputStream.INITIAL_BUFFER_SIZE;
    private int _readBufferSize = ANTLRInputStream.READ_BUFFER_SIZE;

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

    public String getDefaultEncoding() {
        return _defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        _defaultEncoding = defaultEncoding;
    }

    public void compile(InputStream input, OutputStream output) throws IOException {
        try {
            LessCssLexer lexer = new LessCssLexer(createANTLRInputStream(input));
            // todo: look into TokenRewriteStream...might be better fit?
            LessCssParser parser = new LessCssParser(new CommonTokenStream(lexer));
            LessCssParser.styleSheet_return result = parser.styleSheet();
        }
        catch (RecognitionException e) {
            // todo: wrap in our own exception?
            throw new IOExceptionWithCause(e);
        }
    }

    protected ANTLRInputStream createANTLRInputStream(InputStream input) throws IOException {
        String encoding = null;

        // Read a buffer of data to see if we can find the @charset symbol in the beginning of the file.
        PushbackInputStream pushbackStream = new PushbackInputStream(input, getReadBufferSize());
        byte[] buf = new byte[getReadBufferSize()];
        int len = pushbackStream.read(buf, 0, buf.length);
        if (len >= 0) {
            pushbackStream.unread(buf);
            String bufStr = new String(buf, 0, len, "ASCII");
            encoding = parseCharset(bufStr);
        }

        if (encoding == null) {
            encoding = getDefaultEncoding();
        }

        return new ANTLRInputStream(pushbackStream, getInitialBufferSize(), getReadBufferSize(), encoding);
    }

    public static final String CHARSET_SYM = "@charset";
    public static final String NEWLINE_CHARS = "\n\r\f";

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
