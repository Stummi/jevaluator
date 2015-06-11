package org.stummi.evaluator.operator;

import org.stummi.evaluator.instruction.Instruction;


public abstract class AbstractUnaryOperator extends AbstractOperator {

	public AbstractUnaryOperator(int precedence) {
		super(true, false, precedence, -1);
	}
	
	@Override
	public Instruction getBinaryInstruction() {
		throw new UnsupportedOperationException(getClass().getSimpleName() + " cannot be applied binary");
	}

}
