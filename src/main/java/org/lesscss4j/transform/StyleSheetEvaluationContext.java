/**
 * File: StyleSheetEvaluationContext.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 5, 2010
 * Creation Time: 7:28:46 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.transform;

import org.lesscss4j.parser.StyleSheetResource;

public class StyleSheetEvaluationContext extends EvaluationContext {
    private StyleSheetResource _resource;

    public StyleSheetResource getResource() {
        return _resource;
    }

    public void setResource(StyleSheetResource resource) {
        _resource = resource;
    }
}
