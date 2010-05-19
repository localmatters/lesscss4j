/**
 * File: LoggingErrorHandler.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 19, 2010
 * Creation Time: 9:22:25 AM
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingErrorHandler implements ErrorHandler {
    public static final String LESS_CSS_LOG_CATEGORY = "LessCssError";

    private transient static final Log _log = LogFactory.getLog(LESS_CSS_LOG_CATEGORY);

    private boolean _logStackTrace = false;

    public boolean isLogStackTrace() {
        return _logStackTrace;
    }

    public void setLogStackTrace(boolean logStackTrace) {
        _logStackTrace = logStackTrace;
    }

    public void handleError(String message, Throwable exception) {
        if (exception != null) {
            String logMessage = exception.getMessage();
            if (message != null) {
                logMessage = message + ":" + logMessage;
            }

            if (isLogStackTrace()) {
                _log.error(logMessage, exception);
            }
            else {
                _log.error(logMessage);
            }
        }
        else if (message != null) {
            _log.error(message);
        }
        else {
            _log.error("Unknown error");
        }
    }
}
