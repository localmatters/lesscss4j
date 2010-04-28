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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.lesscss4j.model.expression.Expression;

public class DeclarationContainer extends AbstractElement implements VariableContainer {
    private Map<String, Declaration> _declarations;
    private Map<String, Expression> _variables = new LinkedHashMap<String, Expression>();

    public Map<String, Declaration> getDeclarations() {
        return _declarations;
    }

    public Collection<Declaration> getDeclarationList() {
        return _declarations.values();
    }

    public void setDeclarations(Map<String, Declaration> declarations) {
        _declarations = declarations;
    }

    public void addDeclaration(Declaration declaration) {
        if (_declarations == null) {
            _declarations = new LinkedHashMap<String, Declaration>();
        }
        _declarations.put(declaration.getProperty(), declaration);
    }

    public Declaration getDeclaration(String property) {
        return _declarations.get(property);
    }

    public void setVariable(String name, Expression value) {
        _variables.put(name, value);
    }

    public Expression getVariable(String name) {
        return _variables.get(name);
    }

    public Iterator<String> getVariableNames() {
        return _variables.keySet().iterator();
    }
}
