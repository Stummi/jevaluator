package org.stummi.evaluator.operator;

import org.stummi.evaluator.instruction.DivInstruction;
import org.stummi.evaluator.instruction.Instruction;


public class DivideOperator extends AbstractBinaryOperator {

	public DivideOperator() {
		super(2);
	}

	@Override
	public Instruction getBinaryInstruction() {
		return new DivInstruction();
	}

	

}
