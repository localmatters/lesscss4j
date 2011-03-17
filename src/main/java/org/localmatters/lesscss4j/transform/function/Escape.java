/*
Copyright © 2010 to Present, Local Matters, Inc.
All rights reserved.
*/
package org.localmatters.lesscss4j.transform.function;

import org.localmatters.lesscss4j.error.FunctionException;
import org.localmatters.lesscss4j.model.expression.Expression;
import org.localmatters.lesscss4j.model.expression.LiteralExpression;

public class Escape implements Function {
    public Expression evaluate(String name, Expression... args) {
        int numArgs = args.length;
        if (numArgs != 1) {
            throw new FunctionException("Unexpected number of arguments to function %s: %d", name, numArgs);
        }

        String value = args[0].toString();
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            value = value.substring(1, value.length() - 1);
        }
        return new LiteralExpression(value);
    }
}
