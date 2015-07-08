package org.stummi.evaluator.operator;

import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.NegInstruction;
import org.stummi.evaluator.instruction.SubInstruction;

public class MinusOperator extends AbstractOperator {

	public MinusOperator() {
		super(1, 3);
	}

	@Override
	public Instruction getBinaryInstruction() {
		return new SubInstruction();
	}

	@Override
	public Instruction getUnaryInstruction() {
		return new NegInstruction();
	}
	
}
