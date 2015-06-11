package org.stummi.evaluator.exception;

/**
 * Class for Exceptions which occurred during processing of the expression tree
 */
public class ExpressionTreeException extends EvaluatorException {
	private static final long serialVersionUID = -3296775054108302554L;

	public ExpressionTreeException() {
		super();
	}

	public ExpressionTreeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExpressionTreeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpressionTreeException(String message) {
		super(message);
	}

	public ExpressionTreeException(Throwable cause) {
		super(cause);
	}

}
