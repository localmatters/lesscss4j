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

public class AbstractElement implements PositionAware {
    private int _line;
    private int _char;

    public AbstractElement() {
        this(-1, -1);
    }

    public AbstractElement(AbstractElement copy) {
        _line = copy._line;
        _char = copy._char;
    }

    public AbstractElement(int line, int aChar) {
        _line = line;
        _char = aChar;
    }

    public int getLine() {
        return _line;
    }

    public void setLine(int line) {
        _line = line;
    }

    public int getChar() {
        return _char;
    }

    public void setChar(int aChar) {
        _char = aChar;
    }
}
