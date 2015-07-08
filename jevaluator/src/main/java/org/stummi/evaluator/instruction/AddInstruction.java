package org.stummi.evaluator.instruction;

import org.objectweb.asm.Opcodes;

public class AddInstruction extends AbstractBinaryInstruction {

	public AddInstruction() {
		super("add", Opcodes.DADD);
	}

	@Override
	protected Double apply(Double left, Double right) {
		return left+right;
	}

}
