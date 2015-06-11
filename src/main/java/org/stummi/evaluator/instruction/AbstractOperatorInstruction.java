package org.stummi.evaluator.instruction;

import java.io.PrintStream;

import lombok.RequiredArgsConstructor;

import org.objectweb.asm.MethodVisitor;

@RequiredArgsConstructor
public abstract class AbstractOperatorInstruction implements Instruction {

	private final String name;
	private final int opcode;
	
	@Override
	public void dump(PrintStream p) {
		p.println(name);
	}
	
	@Override
	public void visitMethod(MethodVisitor visitor) {
		visitor.visitInsn(opcode);
	}
	
}
