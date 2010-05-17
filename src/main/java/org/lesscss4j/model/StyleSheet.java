/**
 * File: StyleSheet.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:22:26 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.model;

import java.util.ArrayList;
import java.util.List;

public class StyleSheet extends BodyElementContainer {
    private String _charset;
    private List<String> _imports = new ArrayList<String>();

    public String getCharset() {
        return _charset;
    }

    public void setCharset(String charset) {
        _charset = charset;
    }

    public List<String> getImports() {
        return _imports;
    }

    public void setImports(List<String> imports) {
        if (imports == null) {
            _imports.clear();
        }
        else {
            _imports = imports;
        }
    }

    public void addImport(String importValue) {
        if (_imports == null) {
            _imports = new ArrayList<String>();
        }
        _imports.add(importValue);
    }

}
