package org.stummi.evaluator.exception;

import java.io.PrintStream;

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

	public void showPosition(PrintStream out) {
		out.println(expression);
		int l = position;
		while(l-- > 0) {
			out.print(" ");
		}
		out.println("^");
	}
}
