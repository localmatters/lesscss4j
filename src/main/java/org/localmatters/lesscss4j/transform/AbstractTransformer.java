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

package org.localmatters.lesscss4j.transform;

import java.util.Iterator;

import org.localmatters.lesscss4j.error.ErrorUtils;
import org.localmatters.lesscss4j.error.LessCssException;
import org.localmatters.lesscss4j.model.VariableContainer;
import org.localmatters.lesscss4j.model.expression.Expression;
import org.localmatters.lesscss4j.transform.manager.TransformerManager;
import org.localmatters.lesscss4j.transform.manager.TransformerManagerAware;

public abstract class AbstractTransformer<T> implements Transformer<T>, TransformerManagerAware {
    private TransformerManager _transformerManager;

    protected AbstractTransformer() {
        this(null);
    }

    protected AbstractTransformer(TransformerManager transformerManager) {
        _transformerManager = transformerManager;
    }

    public TransformerManager getTransformerManager() {
        return _transformerManager;
    }

    public void setTransformerManager(TransformerManager transformerManager) {
        _transformerManager = transformerManager;
    }

    protected <T> Transformer<T> getTransformer(T obj) {
        return getTransformer(obj, true);
    }

    protected <T> Transformer<T> getTransformer(T obj, boolean required) {
        Transformer<T> transformer = null;
        if (getTransformerManager() != null) {
            transformer = getTransformerManager().getTransformer(obj);
        }

        if (required && transformer == null) {
            throw new IllegalStateException(
                "Unable to find transformer for object of type " + obj.getClass().getName());
        }
        else {
            return transformer;
        }
    }

    protected void evaluateVariables(VariableContainer variableContainer,
                                     VariableContainer transformed,
                                     EvaluationContext context) {
        EvaluationContext varContext = new EvaluationContext(variableContainer, context);
        for (Iterator<String> iter = variableContainer.getVariableNames(); iter.hasNext();) {
            String varName = iter.next();
            Expression varExpression = variableContainer.getVariable(varName);
            try {
                transformed.setVariable(varName, varExpression.evaluate(varContext));
            }
            catch (LessCssException ex) {
                ErrorUtils.handleError(context.getErrorHandler(), varExpression, null, ex);
            }
        }
    }
}
