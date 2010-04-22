/**
 * File: LessCssCompilerImpl.java
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
package org.lesscss4j.compile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.lesscss4j.model.StyleSheet;
import org.lesscss4j.output.StyleSheetWriterImpl;
import org.lesscss4j.output.StyleSheetWriter;
import org.lesscss4j.parser.LessCssStyleSheetParser;
import org.lesscss4j.parser.StyleSheetParser;

public class LessCssCompilerImpl implements LessCssCompiler {
    private StyleSheetParser _styleSheetParser = new LessCssStyleSheetParser();
    private StyleSheetWriter _styleSheetWriter = new StyleSheetWriterImpl();

    public StyleSheetParser getStyleSheetParser() {
        return _styleSheetParser;
    }

    public void setStyleSheetParser(StyleSheetParser styleSheetParser) {
        _styleSheetParser = styleSheetParser;
    }

    public StyleSheetWriter getStyleSheetWriter() {
        return _styleSheetWriter;
    }

    public void setStyleSheetWriter(StyleSheetWriter styleSheetWriter) {
        _styleSheetWriter = styleSheetWriter;
    }


    public void compile(InputStream input, OutputStream output) throws IOException {
        StyleSheet styleSheet = getStyleSheetParser().parse(input);

        // todo: process the stylesheet for LessCSS constructs...or do we do this while writing?

        getStyleSheetWriter().write(output, styleSheet);
    }
}
