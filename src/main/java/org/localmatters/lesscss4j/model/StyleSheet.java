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

public class StyleSheet extends BodyElementContainer {
    private String _charset;
    private List<String> _imports = new ArrayList<String>();

    public String getCharset() {
        return _charset;
    }

    public void setCharset(String charset) {
        _charset = charset;
    }

    public List<String> getImports() {
        return _imports;
    }

    public void setImports(List<String> imports) {
        if (imports == null) {
            _imports.clear();
        }
        else {
            _imports = imports;
        }
    }

    public void addImport(String importValue) {
        if (_imports == null) {
            _imports = new ArrayList<String>();
        }
        _imports.add(importValue);
    }

}
