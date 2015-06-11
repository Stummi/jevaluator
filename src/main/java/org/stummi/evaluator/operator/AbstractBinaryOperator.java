package org.stummi.evaluator.operator;

import org.stummi.evaluator.instruction.Instruction;


public abstract class AbstractBinaryOperator extends AbstractOperator {

	public AbstractBinaryOperator(int precedence) {
		super(false, true, -1, precedence);
	}
	
	@Override
	public Instruction getUnaryInstruction() {
		throw new UnsupportedOperationException(getClass().getSimpleName() + " cannot be applied unary");
	}

}
