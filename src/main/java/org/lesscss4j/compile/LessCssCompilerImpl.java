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
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.lesscss4j.error.ErrorHandler;
import org.lesscss4j.model.StyleSheet;
import org.lesscss4j.output.StyleSheetWriter;
import org.lesscss4j.output.StyleSheetWriterImpl;
import org.lesscss4j.parser.LessCssStyleSheetParser;
import org.lesscss4j.parser.StyleSheetParser;
import org.lesscss4j.parser.StyleSheetResource;
import org.lesscss4j.transform.StyleSheetEvaluationContext;
import org.lesscss4j.transform.StyleSheetTransformer;
import org.lesscss4j.transform.Transformer;

public class LessCssCompilerImpl implements LessCssCompiler {
    private StyleSheetParser _styleSheetParser = new LessCssStyleSheetParser();
    private StyleSheetWriter _styleSheetWriter = new StyleSheetWriterImpl();
    private Transformer<StyleSheet> _styleSheetTransformer;

    public Transformer<StyleSheet> getStyleSheetTransformer() {
        if (_styleSheetTransformer == null) {
            _styleSheetTransformer = StyleSheetTransformer.createDefaultTransformer();
        }
        return _styleSheetTransformer;
    }

    public void setStyleSheetTransformer(Transformer<StyleSheet> styleSheetTransformer) {
        _styleSheetTransformer = styleSheetTransformer;
    }

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

    public void compile(StyleSheetResource input, OutputStream output, ErrorHandler errorHandler) throws IOException {
        if (errorHandler != null && input.getUrl() != null) {
            // Set the context in the error handler to the name of the file we're reading.
            errorHandler.setContext(FilenameUtils.getName(input.getUrl().getPath()));
        }
        StyleSheet styleSheet = getStyleSheetParser().parse(input, errorHandler);

        if (errorHandler == null || errorHandler.getErrorCount() == 0) {
            StyleSheetEvaluationContext context = new StyleSheetEvaluationContext();
            context.setResource(input);
            context.setErrorHandler(errorHandler);

            styleSheet = getStyleSheetTransformer().transform(styleSheet, context).get(0);
        }

        if (errorHandler == null || errorHandler.getErrorCount() == 0) {
            getStyleSheetWriter().write(output, styleSheet, errorHandler);
        }
    }
}
