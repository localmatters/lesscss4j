/**
 * File: DeclarationContainer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:39:38 AM
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

public class DeclarationContainer implements VariableContainer {
    private List<Declaration> _declarations;
    private Map<String, Expression> _variables = new LinkedHashMap<String, Expression>();

    public List<Declaration> getDeclarations() {
        return _declarations;
    }

    public void setDeclarations(List<Declaration> declarations) {
        _declarations = declarations;
    }

    public void addDeclaration(Declaration declaration) {
        if (_declarations == null) {
            _declarations = new ArrayList<Declaration>();
        }
        _declarations.add(declaration);
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
