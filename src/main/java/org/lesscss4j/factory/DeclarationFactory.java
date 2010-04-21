/**
 * File: DeclarationFactory.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 10:29:11 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.factory;

import org.antlr.runtime.tree.Tree;
import org.lesscss4j.model.Declaration;

import static org.lesscss4j.parser.LessCssLexer.*;

public class DeclarationFactory extends AbstractObjectFactory<Declaration> {
    public Declaration create(Tree declarationNode) {
        Declaration declaration = new Declaration();

        for (int idx = 0, numChildren = declarationNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = declarationNode.getChild(idx);
            switch (child.getType()) {
                case IDENT:
                    declaration.setProperty(child.getText());
                    if (child.getChild(0) != null && child.getChild(0).getType() == STAR) {
                        declaration.setStar(true);
                    }
                    break;

                case PROP_VALUE:
                    // todo: probably need a deeper object here
                    String value = createPropValue(child);
                    if (value != null) {
                        declaration.setValue(value);
                    }
                    break;

                case IMPORTANT_SYM:
                    declaration.setImportant(true);
                    break;

                default:
                    handleUnexpectedChild("Unexpected declaration child:", child);
                    break;
            }
        }

        return declaration.getValue() == null ? null : declaration;
    }

    protected String createPropValue(Tree valueNode) {
        StringBuilder value = new StringBuilder();
        for (int idx = 0, numChildren = valueNode.getChildCount(); idx < numChildren; idx++) {
            Tree child = valueNode.getChild(idx);
            if (child.getType() == WS) {
                // todo: some whitespace can be compressed
                value.append(' ');
            }
            else {
                value.append(child.getText());
            }
        }
        return value.toString();
    }
}
