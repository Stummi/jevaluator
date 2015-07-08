package org.stummi.evaluator.instruction;

import org.objectweb.asm.Opcodes;

public class DivInstruction extends AbstractBinaryInstruction {

	public DivInstruction() {
		super("div", Opcodes.DDIV);
		
	}
	
	@Override
	protected Double apply(Double left, Double right) {
		return left/right;
	}

}
