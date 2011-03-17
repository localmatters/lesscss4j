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

package org.localmatters.lesscss4j.model;

import java.util.ArrayList;
import java.util.List;

import org.localmatters.lesscss4j.model.expression.Expression;

public class Declaration extends AbstractElement implements DeclarationElement {
    private String _property;
    private List<Object> _values;
    private boolean _important = false;

    public Declaration() {
    }

    public Declaration(Declaration copy) {
        this(copy, true);
    }

    public Declaration(Declaration copy, boolean copyValues) {
        _property = copy._property;
        _important = copy._important;
        if (copyValues && copy._values != null) {
            _values = new ArrayList<Object>(copy._values.size());
            for (Object value : copy._values) {
                if (value instanceof Expression) {
                    _values.add(((Expression) value).clone());
                }
                else {
                    _values.add(value);
                }
            }
        }
    }

    public String getProperty() {
        return _property;
    }

    public void setProperty(String property) {
        _property = property;
    }

    public List<Object> getValues() {
        return _values;
    }

    public void setValues(List<Object> values) {
        _values = values;
    }

    public void addValue(Object value) {
        if (_values == null) {
            _values = new ArrayList<Object>();
        }
        _values.add(value);
    }

    public boolean isImportant() {
        return _important;
    }

    public void setImportant(boolean important) {
        _important = important;
    }

    public String getValuesAsString() {
        return getValuesAsString(new StringBuilder());
    }

    public String getValuesAsString(StringBuilder buf) {
        for (Object value : getValues()) {
            buf.append(value.toString());
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getProperty());
        buf.append(": ");
        getValuesAsString(buf);
        if (isImportant()) {
            buf.append(" !important");
        }
        return buf.toString();
    }

    @Override
    public Declaration clone() {
        return new Declaration(this);
    }
}
