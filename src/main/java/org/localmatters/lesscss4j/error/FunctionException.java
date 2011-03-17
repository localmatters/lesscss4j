/*
Copyright © 2010 to Present, Local Matters, Inc.
All rights reserved.
*/
package org.localmatters.lesscss4j.error;

public class FunctionException extends ExpressionException {
    public FunctionException() {
    }

    public FunctionException(String message) {
        super(message);
    }

    public FunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionException(Throwable cause) {
        super(cause);
    }

    public FunctionException(String message, Object... args) {
        super(String.format(message, args));
    }
}
