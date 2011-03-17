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

package org.localmatters.lesscss4j.transform.function;

import org.localmatters.lesscss4j.error.FunctionException;
import org.localmatters.lesscss4j.model.expression.ConstantColor;
import org.localmatters.lesscss4j.model.expression.ConstantExpression;
import org.localmatters.lesscss4j.model.expression.ConstantNumber;
import org.localmatters.lesscss4j.model.expression.Expression;

public abstract class AbstractColorFunction implements Function {
    private boolean _valueRequired = true;

    public boolean isValueRequired() {
        return _valueRequired;
    }

    public void setValueRequired(boolean valueRequired) {
        _valueRequired = valueRequired;
    }

    protected ConstantColor getColor(String name, int index, Expression...args) {
        Expression expr = args[index];
        if (!isColor(expr)) {
            throw new FunctionException("Argument %d for function '%s' must be a color: %s", index + 1, name, expr);
        }
        return (ConstantColor) ((ConstantExpression) expr).getValue();
    }

    protected ConstantNumber getNumber(String name, int index, Expression...args) {
        Expression expr = args[index];
        if (!isNumber(expr)) {
            throw new FunctionException("Argument %d for function '%s' must be a number: %s", index + 1, name, expr);
        }
        return (ConstantNumber) ((ConstantExpression) expr).getValue();
    }

    private boolean isNumber(Expression arg) {
        return arg instanceof ConstantExpression &&
               ((ConstantExpression) arg).getValue() instanceof ConstantNumber;
    }

    private boolean isColor(Expression arg) {
        return arg instanceof ConstantExpression &&
               ((ConstantExpression) arg).getValue() instanceof ConstantColor;
    }

    public Expression evaluate(String name, Expression... args) {
        int numArgs = args.length;
        if (isValueRequired() && numArgs != 2 || !isValueRequired() && numArgs != 1) {
            throw new FunctionException("Invalid number of arguments for function '%s'", name);
        }

        ConstantColor color = getColor(name, 0, args);
        ConstantNumber value = null;
        if (args.length > 1) {
            value = getNumber(name, 1, args);
        }

        return evaluate(color, value);
    }

    protected abstract Expression evaluate(ConstantColor color, ConstantNumber value);
}
