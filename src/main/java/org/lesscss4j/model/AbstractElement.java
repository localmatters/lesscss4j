/**
 * File: AbstractElement.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 27, 2010
 * Creation Time: 4:32:16 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.model;

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
