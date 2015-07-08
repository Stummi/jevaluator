package org.stummi.evaluator.exception;

/**
 * Superclassfor Evaluator-Specific exceptions 
 */
public class EvaluatorException extends Exception {
	private static final long serialVersionUID = 1L;

	public EvaluatorException() {
		super();
	}

	public EvaluatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EvaluatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public EvaluatorException(String message) {
		super(message);
	}

	public EvaluatorException(Throwable cause) {
		super(cause);
	}

}
