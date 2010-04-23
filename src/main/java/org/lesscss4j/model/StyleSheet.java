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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lesscss4j.model.expression.Expression;

public class StyleSheet extends BodyElementContainer implements VariableContainer {
    private String _charset;
    private List<String> _imports = new ArrayList<String>();
    private Map<String, Expression> _variables = new LinkedHashMap<String, Expression>();

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
        _imports = imports;
    }

    public void addImport(String importValue) {
        if (_imports == null) {
            _imports = new ArrayList<String>();
        }
        _imports.add(importValue);
    }

    public Expression setVariable(String name, Expression value) {
        return _variables.put(name, value);
    }

    public Expression getVariable(String name) {
        return _variables.get(name);
    }

    public Iterator<String> getVariableNames() {
        return _variables.keySet().iterator();
    }
}
