/**
 * File: StyleSheetResourceLoader.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 18, 2010
 * Creation Time: 10:59:10 AM
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

import java.net.URL;

public class DefaultStyleSheetResourceLoader implements StyleSheetResourceLoader {
    public StyleSheetResource getResource(URL url) {
        if ("file".equals(url.getProtocol())) {
            return new FileStyleSheetResource(url.getPath());
        }
        else {
            return new UrlStyleSheetResource(url);
        }

    }
}
