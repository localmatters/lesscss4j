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

package org.localmatters.lesscss4j.output;

public class PrettyPrintOptions {
    private boolean _singleDeclarationOnOneLine = false;
    private boolean _openingBraceOnNewLine = false;
    private boolean _lineBetweenRuleSets = true;
    private int _indentSize = 4;

    public int getIndentSize() {
        return _indentSize;
    }

    /** Set the number of spaces to indent at each level.  Default = 4 */
    public void setIndentSize(int indentSize) {
        _indentSize = indentSize;
    }

    public boolean isSingleDeclarationOnOneLine() {
        return _singleDeclarationOnOneLine;
    }

    /** If set to true (default = false), rule sets with only one declaration will be printed entirely on one line. */
    public void setSingleDeclarationOnOneLine(boolean singleDeclarationOnOneLine) {
        _singleDeclarationOnOneLine = singleDeclarationOnOneLine;
    }

    public boolean isOpeningBraceOnNewLine() {
        return _openingBraceOnNewLine;
    }

    /** If set to true (default = false), opening braces will be printed on their own line. */
    public void setOpeningBraceOnNewLine(boolean openingBraceOnNewLine) {
        _openingBraceOnNewLine = openingBraceOnNewLine;
    }

    public boolean isLineBetweenRuleSets() {
        return _lineBetweenRuleSets;
    }

    /** If set to true (the default), a blank line is written between rule sets */
    public void setLineBetweenRuleSets(boolean lineBetweenRuleSets) {
        _lineBetweenRuleSets = lineBetweenRuleSets;
    }
}
