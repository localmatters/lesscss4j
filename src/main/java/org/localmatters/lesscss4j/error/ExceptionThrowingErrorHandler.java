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
package org.localmatters.lesscss4j.error;

public class ExceptionThrowingErrorHandler extends AbstractErrorHandler {
    public void handleError(String message, Throwable exception) {
        super.handleError(message, exception);
        if (exception != null) {
            if (message != null) {
                throw new LessCssException(getContextString() + message, exception);
            }
            else {
                throw new LessCssException(getContextString(), exception);
            }
        }
        else if (message != null) {
            throw new LessCssException(getContextString() + message);
        }
        else {
            throw new LessCssException(getContextString() + "Unknown error");
        }
    }
}
