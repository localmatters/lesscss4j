/**
 * File: PageTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:28:24 AM
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

import java.util.Arrays;
import java.util.List;

import org.lesscss4j.model.Page;

public class PageTransformer extends AbstractDeclarationContainerTransformer<Page> {
    public List<Page> transform(Page page, EvaluationContext context) {
        List<Page> pageList = Arrays.asList(new Page(page, false));
        doTransform(page, pageList, context);
        return pageList;
    }
}
