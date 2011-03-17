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
package org.localmatters.lesscss4j.error;

import java.io.PrintWriter;

public class WriterErrorHandler extends AbstractErrorHandler {
    public static final String LESS_CSS_LOG_CATEGORY = "LessCssError";

    private boolean _logStackTrace = false;
    private PrintWriter _writer;

    public PrintWriter getWriter() {
        return _writer;
    }

    public void setWriter(PrintWriter writer) {
        _writer = writer;
    }

    public boolean isLogStackTrace() {
        return _logStackTrace;
    }

    public void setLogStackTrace(boolean logStackTrace) {
        _logStackTrace = logStackTrace;
    }

    public void handleError(String message, Throwable exception) {
        super.handleError(message, exception);
        if (exception != null) {
            String logMessage = exception.getMessage();
            if (message != null) {
                logMessage = message + logMessage;
            }

            getWriter().println(getContextString() + logMessage);
            if (isLogStackTrace()) {
                exception.printStackTrace(getWriter());
            }
        }
        else if (message != null) {
            getWriter().println(getContextString() + message);
        }
        else {
            getWriter().println(getContextString() + "Unknown error");
        }
    }
}