/*
Copyright © 2010 to Present, Local Matters, Inc.
All rights reserved.
*/
package org.lesscss4j.transform.function;

import java.util.IllegalFormatException;

import org.lesscss4j.error.FunctionException;
import org.lesscss4j.model.expression.Expression;
import org.lesscss4j.model.expression.LiteralExpression;

public class FormatFunction implements Function {
    public Expression evaluate(String name, Expression... args) {
        int numArgs = args.length;
        if (numArgs < 1) {
            throw new FunctionException("Function '%s' requires at least one argument", name);
        }

        Expression value = args[0];
        String[] valueArgs;
        if (numArgs > 1) {
            valueArgs = new String[numArgs - 1];
            for (int idx = 1; idx < numArgs; idx++) {
                valueArgs[idx - 1] = args[idx].toString();
            }
        }
        else {
            valueArgs = new String[0];
        }

        try {
            return new LiteralExpression(String.format(value.toString(), valueArgs));
        }
        catch (IllegalFormatException ex) {
            throw new FunctionException(String.format("Invalid format provided for function '%s'", name), ex);
        }
    }
}
