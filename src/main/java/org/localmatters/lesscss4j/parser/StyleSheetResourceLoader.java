/**
 * File: StyleSheetResourceLoader.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 21, 2010
 * Creation Time: 10:08:38 AM
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

import java.net.URL;

public interface StyleSheetResourceLoader {
    StyleSheetResource getResource(URL url);
}
