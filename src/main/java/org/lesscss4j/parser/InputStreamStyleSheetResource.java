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
package org.lesscss4j.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class InputStreamStyleSheetResource implements StyleSheetResource {
    private InputStream _inputStream;

    public InputStreamStyleSheetResource(InputStream inputStream) {
        _inputStream = inputStream;
    }

    public InputStream getInputStream() throws IOException {
        return _inputStream;
    }

    public URL getUrl() {
        return null;
    }
}
