package org.stummi.evaluator.operator;

import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.MulInstruction;


public class MultOperator extends AbstractBinaryOperator {

	public MultOperator() {
		super(2);
	}

	@Override
	public Instruction getBinaryInstruction() {
		return new MulInstruction();
	}


}
