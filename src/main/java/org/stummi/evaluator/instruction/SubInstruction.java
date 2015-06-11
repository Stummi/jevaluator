package org.stummi.evaluator.instruction;

import org.objectweb.asm.Opcodes;

public class SubInstruction extends AbstractBinaryInstruction {

	public SubInstruction() {
		super("sub", Opcodes.DSUB);
	}
	
	@Override
	protected Double apply(Double left, Double right) {
		return left - right;
	}

}
