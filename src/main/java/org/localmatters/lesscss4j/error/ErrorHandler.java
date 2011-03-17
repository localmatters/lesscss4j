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

/**
 * Handles errors during a single run of the LessCss compiler.  ErrorHandlers are inherently not thread safe so a new
 * one should be constructed for each run of the compiler.
 */
public interface ErrorHandler {
    /**
     * Handles the given error.  Either <code>message</code> or <code>exception</code> must be non-null
     *
     * @param message   The message for the error
     * @param exception The exception that caused the error
     */
    void handleError(String message, Throwable exception);

    /**
     * Sets the current context.  The error handler may use this to include context specific information with each error
     * message.  For example, the name of the file currently being parsed might be used as the context.
     *
     * @param context
     */
    void setContext(Object context);

    /**
     * Returns the current context.  The error handler may use this to include context specific information with each
     * error message.  For example, the name of the file currently being parsed might be used as the context.
     *
     * @return The current context
     */
    Object getContext();

    /**
     * Returns the number of errors handled by this handler.
     *
     * @return The number of errors encountered.
     */
    int getErrorCount();
}
