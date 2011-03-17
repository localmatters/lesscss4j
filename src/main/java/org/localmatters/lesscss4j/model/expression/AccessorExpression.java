/**
 * File: AccessorExpression.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 24, 2010
 * Creation Time: 3:40:10 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.localmatters.lesscss4j.model.expression;

import java.util.List;

import org.localmatters.lesscss4j.model.AbstractElement;
import org.localmatters.lesscss4j.model.Declaration;
import org.localmatters.lesscss4j.model.RuleSet;
import org.localmatters.lesscss4j.model.Selector;
import org.localmatters.lesscss4j.transform.EvaluationContext;

public class AccessorExpression extends AbstractElement implements Expression {
    private Selector _selector;
    private String _property;
    private boolean _variable;

    public AccessorExpression() {
    }

    public AccessorExpression(AccessorExpression copy) {
        super(copy);
        _selector = copy._selector.clone();
        _property = copy._property;
        _variable = copy._variable;
    }

    public Selector getSelector() {
        return _selector;
    }

    public void setSelector(Selector selector) {
        _selector = selector;
    }

    public String getProperty() {
        return _property;
    }

    public void setProperty(String property) {
        _property = property;
    }

    public boolean isVariable() {
        return _variable;
    }

    public void setVariable(boolean variable) {
        _variable = variable;
    }

    public Expression evaluate(EvaluationContext context) {
        List<RuleSet> ruleSetList = context.getRuleSet(getSelector());
        if (ruleSetList == null || ruleSetList.size() == 0) {
            // todo: error
        }
        else if (ruleSetList.size() > 1) {
            // todo: error
        }
        else {
            RuleSet ruleSet = ruleSetList.get(0);
            if (isVariable()) {
                Expression var = ruleSet.getVariable(getProperty());
                if (var == null) {
                    // todo: error
                }
                else {
                    return var.evaluate(context);
                }
            }
            else {
                Declaration declaration = ruleSet.getDeclaration(getProperty());
                if (declaration == null) {
                    // todo: error
                }
                else {
                    return new LiteralExpression(declaration.getValuesAsString());
                }
            }
        }
        return this;
    }

    public AccessorExpression clone() {
        return new AccessorExpression(this);
    }
}
