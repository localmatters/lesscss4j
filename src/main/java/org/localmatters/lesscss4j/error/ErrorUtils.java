/*
   Copyright 2010-present Local Matters, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.localmatters.lesscss4j.error;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.RecognitionException;
import org.localmatters.lesscss4j.model.PositionAware;

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
                    new ParseError(formatPosition(error.line, error.charPositionInLine) + " - " +
                                   parser.getErrorMessage(error, parser.getTokenNames()), error));
    }

    public static String formatPosition(int lineNum, int charPos) {
        return "[" + lineNum + ":" + charPos + "]";
    }
}
