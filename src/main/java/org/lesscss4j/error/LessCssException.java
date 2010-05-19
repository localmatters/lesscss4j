/**
 * File: LessCssException.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 19, 2010
 * Creation Time: 9:20:47 AM
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

public class LessCssException extends RuntimeException {
    public LessCssException() {
    }

    public LessCssException(String message) {
        super(message);
    }

    public LessCssException(String message, Throwable cause) {
        super(message, cause);
    }

    public LessCssException(Throwable cause) {
        super(cause);
    }
}
