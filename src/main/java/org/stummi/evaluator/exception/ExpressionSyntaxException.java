package org.stummi.evaluator.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Class for Exceptions which occurred during the tokenize process of the expression
 */
@RequiredArgsConstructor
@Getter
public class ExpressionSyntaxException extends EvaluatorException {
	private static final long serialVersionUID = -9109090921367008289L;
	
	private final String expression;
	private final int position;
	
	public ExpressionSyntaxException(String expression, int position, String message) {
		super(message + " at " + position);
		this.expression = expression;
		this.position = position;
	}
}
