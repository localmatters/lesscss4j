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

package org.localmatters.lesscss4j.model.expression;

import org.localmatters.lesscss4j.model.AbstractElement;
import org.localmatters.lesscss4j.transform.EvaluationContext;

public class LiteralExpression extends AbstractElement implements Expression {
    /** Textual value of the literal expression */
    private String _value;

    /** The token type of this literal as defined in {@link org.localmatters.lesscss4j.parser.antlr.LessCssLexer} */
    private int _type;

    public LiteralExpression() {
    }

    public LiteralExpression(LiteralExpression copy) {
        super(copy);
        _value = copy._value;
        _type = copy._type;
    }

    public LiteralExpression(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        _type = type;
    }

    public Expression evaluate(EvaluationContext context) {
        return this;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public LiteralExpression clone() {
        return new LiteralExpression(this);
    }

    public boolean isString() {
        if (_value.length() >= 2) {
            char first = _value.charAt(0);
            if (first == '"' || first == '\'' && first == _value.charAt(_value.length() - 1)) {
                return true;
            }
        }
        return false;
    }
}
