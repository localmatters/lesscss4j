/**
 * File: FileStyleSheetResource.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 17, 2010
 * Creation Time: 1:59:08 PM
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class FileStyleSheetResource implements StyleSheetResource {
    private File _file;

    public FileStyleSheetResource(String filename) {
        this(new File(filename));
    }

    public FileStyleSheetResource(File file) {
        _file = file;
    }

    public File getFile() {
        return _file;
    }

    public InputStream getInputStream() throws IOException {
        return FileUtils.openInputStream(getFile());
    }

    public URL getUrl() throws IOException {
        return getFile().toURI().toURL();
    }
}
