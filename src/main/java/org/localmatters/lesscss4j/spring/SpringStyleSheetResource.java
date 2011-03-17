/**
 * File: SpringStyleSheetResource.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 18, 2010
 * Creation Time: 8:01:19 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.localmatters.lesscss4j.spring;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.localmatters.lesscss4j.parser.StyleSheetResource;
import org.springframework.core.io.Resource;

public class SpringStyleSheetResource implements StyleSheetResource {
    private Resource _resource;

    public SpringStyleSheetResource(Resource resource) {
        _resource = resource;
    }

    public Resource getResource() {
        return _resource;
    }

    public InputStream getInputStream() throws IOException {
        return getResource().getInputStream();
    }

    public URL getUrl() throws IOException {
        return getResource().getURL();
    }
}
