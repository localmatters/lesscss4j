/**
 * File: AbstractErrorHandler.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 19, 2010
 * Creation Time: 10:01:45 AM
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

public class AbstractErrorHandler implements ErrorHandler {
    private int _errorCount = 0;
    private Object _context;

    public Object getContext() {
        return _context;
    }

    public String getContextString() {
        return _context != null ? _context.toString() + ' ' : "";
    }

    public void setContext(Object context) {
        _context = context;
    }

    public void handleError(String message, Throwable exception) {
        _errorCount++;
    }

    public int getErrorCount() {
        return _errorCount;
    }
}
