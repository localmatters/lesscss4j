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

import org.antlr.runtime.tree.Tree;
import org.localmatters.lesscss4j.error.ErrorHandler;
import org.localmatters.lesscss4j.model.Selector;

import static org.localmatters.lesscss4j.parser.antlr.LessCssLexer.WS;

public class SelectorFactory extends AbstractObjectFactory<Selector> {
    public Selector create(Tree selectorNode, ErrorHandler errorHandler) {
        Selector selector = null;
        
        String selectorText = concatChildNodeText(selectorNode);
        if (selectorText.length() > 0) {
            selector = new Selector(selectorText.toString());
            selector.setLine(selectorNode.getLine());
            selector.setChar(selectorNode.getCharPositionInLine());
        }
        return selector; 
    }
}
