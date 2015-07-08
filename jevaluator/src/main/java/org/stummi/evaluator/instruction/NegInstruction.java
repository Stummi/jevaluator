package org.stummi.evaluator.instruction;

import org.objectweb.asm.Opcodes;

public class NegInstruction extends AbstractUnaryInstruction {

	public NegInstruction() {
		super("neg", Opcodes.DNEG);
	}
	
	@Override
	protected Double apply(Double right) {
		return -right;
	}

}
