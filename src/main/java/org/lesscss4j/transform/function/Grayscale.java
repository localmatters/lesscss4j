/*
Copyright © 2010 to Present, Local Matters, Inc.
All rights reserved.
*/
package org.lesscss4j.transform.function;

import org.lesscss4j.model.expression.ConstantColor;
import org.lesscss4j.model.expression.ConstantNumber;
import org.lesscss4j.model.expression.Expression;

public class Grayscale extends Desaturate {
    public Grayscale() {
        setValueRequired(false);
    }

    @Override
    protected Expression evaluate(ConstantColor color, ConstantNumber value) {
        return super.evaluate(color, new ConstantNumber(100, "%"));
    }
}
