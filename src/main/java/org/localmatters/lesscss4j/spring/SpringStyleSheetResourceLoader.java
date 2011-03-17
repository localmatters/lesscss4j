/**
 * File: SpringStyleSheetResourceLoader.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 24, 2010
 * Creation Time: 11:11:37 AM
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

import java.net.URL;

import org.localmatters.lesscss4j.parser.StyleSheetResource;
import org.localmatters.lesscss4j.parser.StyleSheetResourceLoader;
import org.springframework.core.io.ResourceLoader;

public class SpringStyleSheetResourceLoader implements StyleSheetResourceLoader {
    ResourceLoader _resourceLoader;

    public ResourceLoader getResourceLoader() {
        return _resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        _resourceLoader = resourceLoader;
    }

    public StyleSheetResource getResource(URL url) {
        return new SpringStyleSheetResource(getResourceLoader().getResource(url.toString()));
    }
}
