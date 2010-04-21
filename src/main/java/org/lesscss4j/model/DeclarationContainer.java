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
import java.util.List;

public class DeclarationContainer {
    private List<Declaration> _declarations;

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
}
