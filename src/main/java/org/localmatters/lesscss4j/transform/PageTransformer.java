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
package org.localmatters.lesscss4j.transform;

import java.util.Arrays;
import java.util.List;

import org.localmatters.lesscss4j.model.Page;
import org.localmatters.lesscss4j.transform.manager.TransformerManager;

public class PageTransformer extends AbstractDeclarationContainerTransformer<Page> {
    public PageTransformer() {
    }

    public PageTransformer(TransformerManager transformerManager) {
        super(transformerManager);
    }

    public List<Page> transform(Page page, EvaluationContext context) {
        List<Page> pageList = Arrays.asList(new Page(page, false));
        doTransform(page, pageList, context);
        return pageList;
    }
}
