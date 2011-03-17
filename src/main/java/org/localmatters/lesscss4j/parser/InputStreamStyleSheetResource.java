/**
 * File: InputStreamStyleSheetResource.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 17, 2010
 * Creation Time: 1:57:30 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.localmatters.lesscss4j.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class InputStreamStyleSheetResource implements StyleSheetResource {
    private InputStream _inputStream;
    private URL _url;

    public InputStreamStyleSheetResource(InputStream inputStream) {
        this(inputStream, null);
    }

    public InputStreamStyleSheetResource(InputStream inputStream, URL url) {
        _inputStream = inputStream;
        _url = url;
    }

    public InputStream getInputStream() throws IOException {
        return _inputStream;
    }

    public URL getUrl() {
        return _url;
    }
}
