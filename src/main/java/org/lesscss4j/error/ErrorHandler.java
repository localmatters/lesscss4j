/**
 * File: ErrorHandler.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 19, 2010
 * Creation Time: 9:14:47 AM
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
