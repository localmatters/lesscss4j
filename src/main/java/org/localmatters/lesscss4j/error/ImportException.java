/**
 * File: ImportException.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 26, 2010
 * Creation Time: 3:17:48 PM
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

public class ImportException extends LessCssException {
    private String _url;

    public ImportException(String url) {
        super("Import error: " + url);
        _url = url;
    }

    public ImportException(String url, Throwable exception) {
        super("Import error: " + url, exception);
        _url = url;
    }

    public ImportException(String message, String url, Throwable exception) {
        super("Import error: " + url + ": " + message, exception);
        _url = url;
    }
}
