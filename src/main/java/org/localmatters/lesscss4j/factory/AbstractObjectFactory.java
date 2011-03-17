/*
   Copyright 2010-present Local Matters, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.localmatters.lesscss4j.factory;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import org.localmatters.lesscss4j.parser.antlr.LessCssParser;

import static org.localmatters.lesscss4j.parser.antlr.LessCssLexer.*;

public abstract class AbstractObjectFactory<T> implements ObjectFactory<T> {
    protected String formatNode(String prefix, Tree node) {
        return String.format("%s [%d=%s] %d:%d - %s",
                             prefix, node.getType(), LessCssParser.tokenNames[node.getType()],  
                             node.getLine(), node.getCharPositionInLine(),
                             node.toString());
    }

    protected void handleUnexpectedChild(String prefix, Tree child) {
        int type = child.getType();
        if (type != WS && type != EOF) {
            throw new IllegalStateException(formatNode(prefix, child));
        }
    }

    protected String concatChildNodeText(Tree parent) {
        StringBuilder buf = new StringBuilder();
        for (int idx = 0, numChildren = parent.getChildCount(); idx < numChildren; idx++) {
            Tree child = parent.getChild(idx);
            if (child.getType() == WS) {
                // Compress all whitespace into a single space
                buf.append(' ');
            }
            else {
                buf.append(child.getText());
            }
        }
        return buf.toString();
    }
}
