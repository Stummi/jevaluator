package org.stummi.evaluator.operator;

import org.stummi.evaluator.instruction.AddInstruction;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.NOPInstruction;

public class PlusOperator extends AbstractOperator {

	public PlusOperator() {
		super(1, 3);
	}

	@Override
	public Instruction getBinaryInstruction() {
		return new AddInstruction();
	}

	@Override
	public Instruction getUnaryInstruction() {
		return new NOPInstruction();
	}
	

}
