package org.stummi.evaluator.instruction;

import java.io.PrintStream;

import org.objectweb.asm.MethodVisitor;
import org.stummi.evaluator.EvaluationContext;

public class NOPInstruction implements Instruction {

	@Override
	public void dump(PrintStream p) {}

	@Override
	public void visitMethod(MethodVisitor visitor) {}

	@Override
	public void run(EvaluationContext context) {}

	
}
