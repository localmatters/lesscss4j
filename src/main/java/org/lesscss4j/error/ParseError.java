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
package org.lesscss4j.error;

import org.antlr.runtime.RecognitionException;

public class ParseError extends LessCssException {
    public ParseError(String message, RecognitionException exception) {
        super(message, exception);
    }
}
