package org.stummi.evaluator.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractOperator implements Operator{
	
	private final boolean unary, binary;
	private final int unaryPrecedence, binaryPrecedence;
	
	public AbstractOperator(int unaryPrecedence, int binaryPrecedence) {
		this.unary = true;
		this.binary = true;
		this.unaryPrecedence = unaryPrecedence;
		this.binaryPrecedence = binaryPrecedence;
	}
}
