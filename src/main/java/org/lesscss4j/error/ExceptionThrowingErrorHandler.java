/**
 * File: ExceptionThrowingErrorHandler.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 19, 2010
 * Creation Time: 9:19:56 AM
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

public class ExceptionThrowingErrorHandler implements ErrorHandler {
    public void handleError(String message, Throwable exception) {
        if (exception != null) {
            if (message != null) {
                throw new LessCssException(message, exception);
            }
            else {
                throw new LessCssException(exception);
            }
        }
        else if (message != null) {
            throw new LessCssException(message);
        }
        else {
            throw new LessCssException("Unknown error");
        }
    }
}
