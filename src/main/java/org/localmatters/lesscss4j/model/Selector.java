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

public class Selector extends AbstractElement implements Cloneable {
    String _text;

    public Selector() {
        this((String)null);
    }

    public Selector(String text) {
        _text = text;
    }

    public Selector(Selector... selectors) {
        StringBuilder buf = new StringBuilder();
        for (Selector selector : selectors) {
            if (buf.length() > 0 && selector.getText().charAt(0) != ':') {
                buf.append(' ');
            }
            buf.append(selector.getText());
        }
        setText(buf.toString());
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Selector that = (Selector) obj;
        return getText().equals(that.getText());
    }

    @Override
    public int hashCode() {
        return _text.hashCode();
    }

    @Override
    public Selector clone() {
        return new Selector(this);
    }
}
