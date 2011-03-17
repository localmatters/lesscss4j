/*
Copyright © 2010 to Present, Local Matters, Inc.
All rights reserved.
*/
package org.localmatters.lesscss4j.transform.function;

import org.localmatters.lesscss4j.model.expression.Expression;

public interface Function {
    Expression evaluate(String name, Expression... args);
}
