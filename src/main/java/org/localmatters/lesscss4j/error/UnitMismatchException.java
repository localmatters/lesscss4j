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

package org.localmatters.lesscss4j.error;

import org.localmatters.lesscss4j.model.expression.ConstantValue;

public class UnitMismatchException extends LessCssException {
    private ConstantValue _left;
    private ConstantValue _right;

    public UnitMismatchException(ConstantValue left, ConstantValue right) {
        super("Unit mismatch: " + left.toString() + " " + right.toString());
        _left = left;
        _right = right;
    }

    public UnitMismatchException(String message, ConstantValue left, ConstantValue right) {
        super("Unit mismatch: " + message + ": " + left.toString() + " " + right.toString());
        _left = left;
        _right = right;
    }

    public UnitMismatchException(String message, ConstantValue left, ConstantValue right, Throwable cause) {
        super("Unit mismatch:" + message + ": " + left.toString() + " " + right.toString(), cause);
        _left = left;
        _right = right;
    }

    public UnitMismatchException(ConstantValue left, ConstantValue right, Throwable cause) {
        super("Unit mismatch:" + left.toString() + " " + right.toString(),cause);
        _left = left;
        _right = right;
    }

    public ConstantValue getLeft() {
        return _left;
    }

    public ConstantValue getRight() {
        return _right;
    }
}
