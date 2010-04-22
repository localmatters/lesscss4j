/**
 * File: PrettyPrintOptions.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 22, 2010
 * Creation Time: 7:29:27 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.output;

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
