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

import org.lesscss4j.model.AbstractElement;
import org.lesscss4j.transform.EvaluationContext;

// todo: extend ListExpression
public class FunctionExpression extends AbstractElement implements Expression {
    private String _name;
    private List<Expression> _arguments;
    private boolean _quoted = false; // for IE specific stuff

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

    public boolean isQuoted() {
        return _quoted;
    }

    public void setQuoted(boolean quoted) {
        _quoted = quoted;
    }

    public Expression evaluate(EvaluationContext context) {
        return new LiteralExpression(toString(context));
    }

    public String toString(EvaluationContext context) {
        StringBuilder buf = new StringBuilder();
        if (isQuoted()) {
            buf.append("\"");
        }
        buf.append(getName());
        buf.append("(");
        if (getArguments() != null) {
            for (Expression expression : getArguments()) {
                if (context != null) {
                    buf.append(expression.evaluate(context).toString());
                }
                else {
                    buf.append(expression.toString());
                }
            }
        }
        buf.append(')');
        if (isQuoted()) {
            buf.append("\"");
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        return toString(null);
    }
}
