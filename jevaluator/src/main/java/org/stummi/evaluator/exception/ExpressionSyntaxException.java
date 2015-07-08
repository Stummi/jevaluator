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
	private final int startPosition;
	private final int position;
	
	public ExpressionSyntaxException(String expression, int startPosition, int position, String message) {
		super(message);
		this.expression = expression;
		this.position = position;
		this.startPosition = startPosition;
	}

	public void showPosition(PrintStream out) {
		out.println(expression);
		int p = 0;
		while(p++ < startPosition) {
			out.print(" ");
		}
		
		out.print("^");
		while(p++ < position) {
			out.print("~");
		}
		out.println();
	}
}
