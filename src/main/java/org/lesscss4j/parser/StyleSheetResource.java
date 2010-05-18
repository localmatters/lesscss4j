/**
 * File: StyleSheetResource.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 17, 2010
 * Creation Time: 1:49:41 PM
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

public interface StyleSheetResource {
    InputStream getInputStream() throws IOException;
    URL getUrl() throws IOException;
}
