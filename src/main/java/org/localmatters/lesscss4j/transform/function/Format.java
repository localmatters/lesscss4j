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
import org.localmatters.lesscss4j.model.expression.ConstantExpression;
import org.localmatters.lesscss4j.model.expression.ConstantNumber;
import org.localmatters.lesscss4j.model.expression.Expression;
import org.localmatters.lesscss4j.model.expression.LiteralExpression;

public class Format implements Function {
    public Expression evaluate(String name, Expression... args) {
        int numArgs = args.length;
        if (numArgs < 1) {
            throw new FunctionException("Function '%s' requires at least one argument", name);
        }

        Expression value = args[0];

        StringBuilder result = new StringBuilder();

        // We have our own scaled down version of String.format here because String.format won't cast the arguments
        // into the correct type.  For example, String.format("%d", new Double(123)) will throw an exception.
        String valueStr = value.toString();
        int index = valueStr.indexOf('%');
        int lastIndex = 0;
        int argIndex = 1;
        while (index >= 0) {
            result.append(valueStr, lastIndex, index);
            index++;
            if (index < valueStr.length()) {
                char formatCode = valueStr.charAt(index);
                if (formatCode != '%' && argIndex >= args.length) {
                    throw new FunctionException("Not enough arguments provided for function '%s' with format: %s",
                                                name, valueStr);
                }

                switch (formatCode) {
                    case '%':
                        result.append('%');
                        break;

                    case 's':
                        result.append(getArgValue(args[argIndex++]).toString());
                        break;

                    case 'd': {
                        Object argValue = getArgValue(args[argIndex++]);
                        if (argValue instanceof Number) {
                            result.append(((Number) argValue).intValue());
                        }
                        else {
                            throw new FunctionException("Invalid format provided for function '%s': %s",
                                                        name, valueStr);
                        }
                        break;
                    }

                    default:
                        throw new FunctionException("Invalid format provided for function '%s': %s", name, valueStr);
                }
            }
            lastIndex = index + 1;
            index = valueStr.indexOf('%', lastIndex);
        }

        result.append(valueStr, lastIndex, valueStr.length());

        return new LiteralExpression(result.toString());
    }

    protected Object getArgValue(Expression arg) {
        Object argValue = arg.toString();
        if (arg instanceof LiteralExpression && ((LiteralExpression) arg).isString()) {
            argValue = ((String) argValue).substring(1, ((String) argValue).length() - 1);
        }
        if (arg instanceof ConstantExpression && ((ConstantExpression) arg).getValue() instanceof ConstantNumber) {
            argValue = ((ConstantExpression) arg).getValue().getValue();
        }

        return argValue;
    }
}
