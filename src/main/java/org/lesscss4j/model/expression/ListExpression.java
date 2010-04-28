/**
 * File: ListExpression.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 23, 2010
 * Creation Time: 12:58:51 PM
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

public class ListExpression extends AbstractElement implements Expression {
    private List<Expression> _expressions;

    public List<Expression> getExpressions() {
        return _expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        _expressions = expressions;
    }

    public void addExpression(Expression expression) {
        if (_expressions == null) {
            _expressions = new ArrayList<Expression>();
        }
        _expressions.add(expression);
    }

    public Expression evaluate(EvaluationContext context) {
        StringBuilder buf = new StringBuilder();
        for (Expression expression : _expressions) {
            buf.append(expression.evaluate(context).toString());
        }
        return new LiteralExpression(buf.toString());
    }
}
