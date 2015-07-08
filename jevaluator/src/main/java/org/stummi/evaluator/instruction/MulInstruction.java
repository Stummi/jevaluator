package org.stummi.evaluator.instruction;

import org.objectweb.asm.Opcodes;

public class MulInstruction extends AbstractBinaryInstruction {

	public MulInstruction() {
		super("mul", Opcodes.DMUL);
	}
	
	@Override
	protected Double apply(Double left, Double right) {
		return left * right;
	}

}
