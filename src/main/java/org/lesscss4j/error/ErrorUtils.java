/**
 * File: ErrorUtils.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 19, 2010
 * Creation Time: 10:31:24 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.error;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.RecognitionException;
import org.lesscss4j.model.PositionAware;

public abstract class ErrorUtils {

    public static void handleError(ErrorHandler errorHandler, LessCssException error) {
        handleError(errorHandler, null, null, error);

    }

    public static void handleError(ErrorHandler errorHandler, String message, LessCssException error) {
        handleError(errorHandler, null, message, error);
    }

    public static void handleError(ErrorHandler errorHandler,
                                   PositionAware pos,
                                   LessCssException error) {
        handleError(errorHandler, pos, null, error);
    }

    public static void handleError(ErrorHandler errorHandler,
                                   PositionAware pos,
                                   String message,
                                   LessCssException error) {
        if (errorHandler != null) {
            if (error.getPosition() != null) {
                pos = error.getPosition();
            }
            
            if (pos != null) {
                message = formatPosition(pos.getLine(), pos.getChar()) + " - " + (message == null ? "" : message);
            }
            errorHandler.handleError(message, error);
        }
        else {
            throw error;
        }
    }

    public static void handleError(ErrorHandler errorHandler, RecognitionException error, BaseRecognizer parser) {
        handleError(errorHandler, null, null,
                    new ParseError(error, formatPosition(error.line, error.charPositionInLine),
                                   parser.getErrorMessage(error, parser.getTokenNames())));
    }

    public static String formatPosition(int lineNum, int charPos) {
        return "[" + lineNum + ":" + charPos + "]";
    }
}
