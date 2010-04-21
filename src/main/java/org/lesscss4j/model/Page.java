/**
 * File: Page.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 11:38:09 AM
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

public class Page extends DeclarationContainer implements BodyElement {
    private String _pseudoPage;

    public String getPseudoPage() {
        return _pseudoPage;
    }

    public void setPseudoPage(String pseudoPage) {
        _pseudoPage = pseudoPage;
    }
}
