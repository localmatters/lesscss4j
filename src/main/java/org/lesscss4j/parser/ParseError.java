/**
 * File: ParseError.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 26, 2010
 * Creation Time: 3:12:24 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.parser;

import org.antlr.runtime.RecognitionException;

public class ParseError {
    private RecognitionException _exception;
    private String[] _tokenNames;
    private String _header;
    private String _message;

    public RecognitionException getException() {
        return _exception;
    }

    public void setException(RecognitionException exception) {
        _exception = exception;
    }

    public String[] getTokenNames() {
        return _tokenNames;
    }

    public void setTokenNames(String[] tokenNames) {
        _tokenNames = tokenNames;
    }

    public String getHeader() {
        return _header;
    }

    public void setHeader(String header) {
        _header = header;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }
}
