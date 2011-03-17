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
package org.localmatters.lesscss4j.compile;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.localmatters.lesscss4j.error.ErrorHandler;
import org.localmatters.lesscss4j.model.StyleSheet;
import org.localmatters.lesscss4j.output.StyleSheetWriter;
import org.localmatters.lesscss4j.output.StyleSheetWriterImpl;
import org.localmatters.lesscss4j.parser.LessCssStyleSheetParser;
import org.localmatters.lesscss4j.parser.StyleSheetParser;
import org.localmatters.lesscss4j.parser.StyleSheetResource;
import org.localmatters.lesscss4j.transform.StyleSheetEvaluationContext;
import org.localmatters.lesscss4j.transform.Transformer;
import org.localmatters.lesscss4j.transform.manager.TransformerManager;

public class LessCssCompilerImpl implements LessCssCompiler {
    private StyleSheetParser _styleSheetParser = new LessCssStyleSheetParser();
    private StyleSheetWriter _styleSheetWriter = new StyleSheetWriterImpl();
    private TransformerManager _transformerManager;

    public TransformerManager getTransformerManager() {
        return _transformerManager;
    }

    public void setTransformerManager(TransformerManager transformerManager) {
        _transformerManager = transformerManager;
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
        if (getTransformerManager() == null) {
            throw new IllegalStateException("No TransformerManager defined in compiler.");
        }
        if (errorHandler != null && input.getUrl() != null) {
            // Set the context in the error handler to the name of the file we're reading.
            errorHandler.setContext(FilenameUtils.getName(input.getUrl().getPath()));
        }
        StyleSheet styleSheet = getStyleSheetParser().parse(input, errorHandler);

        if (errorHandler == null || errorHandler.getErrorCount() == 0) {
            StyleSheetEvaluationContext context = new StyleSheetEvaluationContext();
            context.setResource(input);
            context.setErrorHandler(errorHandler);

            Transformer<StyleSheet> styleSheetTransformer = getTransformerManager().getTransformer(styleSheet);
            if (styleSheetTransformer == null) {
                throw new IllegalStateException("No transformer found for class: " + styleSheet.getClass().getName());
            }
            styleSheet = styleSheetTransformer.transform(styleSheet, context).get(0);
        }

        if (errorHandler == null || errorHandler.getErrorCount() == 0) {
            getStyleSheetWriter().write(output, styleSheet, errorHandler);
        }
    }
}
