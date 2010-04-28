/**
 * File: AbstractDeclarationContainerTransformer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 8:31:55 AM
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

import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.DeclarationContainer;
import org.lesscss4j.model.expression.EvaluationContext;

public abstract class AbstractDeclarationContainerTransformer<T extends DeclarationContainer>
    extends AbstractTransformer<T> {

    private Transformer<Declaration> _declarationTransformer;

    public Transformer<Declaration> getDeclarationTransformer() {
        return _declarationTransformer;
    }

    public void setDeclarationTransformer(Transformer<Declaration> declarationTransformer) {
        _declarationTransformer = declarationTransformer;
    }

    protected void transformDeclarations(DeclarationContainer container, EvaluationContext context) {
        EvaluationContext declContext = new EvaluationContext(container, context);
        for (Declaration declaration : container.getDeclarationList()) {
            getDeclarationTransformer().transform(declaration, declContext);
        }
    }
}
