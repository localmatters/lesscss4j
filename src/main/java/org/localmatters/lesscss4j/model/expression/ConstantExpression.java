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

public class ConstantExpression extends AbstractElement implements Expression {
    public static final String UNIT_COLOR = "color";

    private ConstantValue _value;

    public ConstantExpression() {
        this((ConstantValue)null);
    }

    public ConstantExpression(ConstantExpression copy) {
        super(copy);
        _value = copy._value.clone();
    }

    public ConstantExpression(ConstantValue value) {
        _value = value;
    }

    public ConstantExpression(String value) {
        if (value.charAt(0) == '#' || ConstantColor.isColorFunction(value)) {
            _value = new ConstantColor(value);
        }
        else {
            _value = new ConstantNumber(value);
        }
    }

    public ConstantValue getValue() {
        return _value;
    }

    public void setValue(ConstantValue value) {
        _value = value;
    }

    public Expression evaluate(EvaluationContext context) {
        // Evaluation of a constant is itself
        return this;
    }

    public ConstantExpression clone() {
        return new ConstantExpression(this);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
