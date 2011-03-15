/*
Copyright © 2010 to Present, Local Matters, Inc.
All rights reserved.
*/
package org.lesscss4j.transform.function;

import org.lesscss4j.error.FunctionException;
import org.lesscss4j.model.expression.ConstantColor;
import org.lesscss4j.model.expression.ConstantExpression;
import org.lesscss4j.model.expression.ConstantNumber;
import org.lesscss4j.model.expression.Expression;

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
