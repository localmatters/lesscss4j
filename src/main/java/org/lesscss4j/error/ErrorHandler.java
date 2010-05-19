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

public interface ErrorHandler {
    /**
     * Handles the given error.  Either <code>message</code> or <code>exception</code> must be non-null
     *
     * @param message   The message for the error
     * @param exception The exception that caused the error
     */
    void handleError(String message, Throwable exception);

    /**
     * Returns the number of errors handled by this handler.
     *
     * @return The number of errors encountered.
     */
    int getErrorCount();
}
