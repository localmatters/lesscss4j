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
