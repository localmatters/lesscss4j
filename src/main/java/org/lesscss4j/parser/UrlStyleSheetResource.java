/**
 * File: UrlStyleSheetResource.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 17, 2010
 * Creation Time: 1:50:40 PM
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
import java.net.URLConnection;

public class UrlStyleSheetResource implements StyleSheetResource {
    private URL _url;

    public UrlStyleSheetResource(URL url) {
        _url = url;
    }

    public InputStream getInputStream() throws IOException {
        URLConnection con = getUrl().openConnection();
        con.setUseCaches(false);
        return con.getInputStream();
    }

    public URL getUrl() {
        return _url;
    }
}
