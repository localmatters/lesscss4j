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

package org.localmatters.lesscss4j.model.expression;

import java.util.ArrayList;
import java.util.List;

import org.localmatters.lesscss4j.model.AbstractElement;
import org.localmatters.lesscss4j.transform.EvaluationContext;

public class FunctionExpression extends AbstractElement implements Expression {
    private String _name;
    private List<Expression> _arguments;
    private boolean _quoted = false; // for IE specific stuff

    public FunctionExpression() {
    }

    public FunctionExpression(FunctionExpression copy) {
        super(copy);
        _name = copy._name;
        _quoted = copy._quoted;
        if (copy._arguments != null) {
            _arguments = new ArrayList<Expression>(copy._arguments.size());
            for (Expression argument : copy._arguments) {
                _arguments.add(argument.clone());
            }
        }
    }

    public FunctionExpression(String name) {
        this();
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

    public FunctionExpression clone() {
        return new FunctionExpression(this);
    }
}
