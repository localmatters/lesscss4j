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

import org.localmatters.lesscss4j.error.UndefinedVariableException;
import org.localmatters.lesscss4j.model.AbstractElement;
import org.localmatters.lesscss4j.transform.EvaluationContext;

public class VariableReferenceExpression extends AbstractElement implements Expression {
    private String _variableName;

    public VariableReferenceExpression() {
        this((String)null);
    }

    public VariableReferenceExpression(VariableReferenceExpression copy) {
        super(copy);
        _variableName = copy._variableName;
    }

    public VariableReferenceExpression(String variableName) {
        _variableName = variableName;
    }

    public String getVariableName() {
        return _variableName;
    }

    public void setVariableName(String variableName) {
        _variableName = variableName;
    }

    public Expression evaluate(EvaluationContext context) {
        Expression value = context.getVariable(getVariableName());
        if (value == null) {
            throw new UndefinedVariableException(this);
        }
        return value.evaluate(context);
    }

    public VariableReferenceExpression clone() {
        return new VariableReferenceExpression(this);
    }
}
