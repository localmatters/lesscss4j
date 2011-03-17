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

package org.localmatters.lesscss4j.model;

import java.util.ArrayList;
import java.util.List;

import org.localmatters.lesscss4j.model.expression.Expression;

public class MixinReference extends AbstractElement implements DeclarationElement {
    private Selector _selector;
    private List<Expression> _arguments = new ArrayList<Expression>();

    public MixinReference() {
    }

    public MixinReference(MixinReference copy) {
        _selector = copy._selector.clone();
        for (Expression argument : copy._arguments) {
            _arguments.add(argument.clone());
        }
    }

    public Selector getSelector() {
        return _selector;
    }

    public void setSelector(Selector selector) {
        _selector = selector;
    }

    public List<Expression> getArguments() {
        return _arguments;
    }

    public void addArgument(Expression expression) {
        if (_arguments == null) {
            _arguments = new ArrayList<Expression>();
        }
        _arguments.add(expression);
    }

    @Override
    public String toString() {
        return getSelector().toString();
    }

    @Override
    public MixinReference clone() {
        return new MixinReference(this);
    }
}
