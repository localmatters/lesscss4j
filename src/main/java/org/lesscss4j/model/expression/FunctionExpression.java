/**
 * File: FunctionExpression.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 4:27:41 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.model.expression;

import java.util.ArrayList;
import java.util.List;

// todo: extend ListExpression
public class FunctionExpression implements Expression {
    private String _name;
    private List<Expression> _arguments;

    public FunctionExpression() {
        this(null);
    }

    public FunctionExpression(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public List<Expression> getArguments() {
        return _arguments;
    }

    public void setArguments(List<Expression> arguments) {
        _arguments = arguments;
    }

    public void addArgument(Expression arg) {
        if (_arguments == null) {
            _arguments = new ArrayList<Expression>();
        }
        _arguments.add(arg);
    }

    public Expression evaluate(EvaluationContext context) {
        StringBuilder buf = new StringBuilder(getName());
        buf.append("(");
        for (Expression expression : getArguments()) {
            buf.append(expression.evaluate(context).toString());
        }
        buf.append(')');
        return new LiteralExpression(buf.toString());
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(getName());
        buf.append('(');
        for (Expression arg : getArguments()) {
            buf.append(arg.toString());
        }
        buf.append(')');
        return buf.toString();
    }
}
