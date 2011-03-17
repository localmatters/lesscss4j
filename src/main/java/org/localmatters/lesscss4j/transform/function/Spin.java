/*
Copyright © 2010 to Present, Local Matters, Inc.
All rights reserved.
*/
package org.localmatters.lesscss4j.transform.function;

import org.localmatters.lesscss4j.model.expression.ConstantColor;
import org.localmatters.lesscss4j.model.expression.ConstantExpression;
import org.localmatters.lesscss4j.model.expression.ConstantNumber;
import org.localmatters.lesscss4j.model.expression.Expression;

public class Spin extends AbstractColorFunction {
    @Override
    protected Expression evaluate(ConstantColor color, ConstantNumber value) {
        float[] hsla = color.toHSL();

        ConstantColor newColor = new ConstantColor();
        newColor.setHSL(((hsla[0] + (float)value.getValue()) % 360.0f), hsla[1], hsla[2]);
        return new ConstantExpression(newColor);
    }
}
