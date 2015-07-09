package org.stummi.evaluator.instruction;

import java.io.PrintStream;

import org.objectweb.asm.MethodVisitor;
import org.stummi.evaluator.EvaluationContext;
import org.stummi.evaluator.asm.ASMParseContext;

public class NOPInstruction implements Instruction {

	@Override
	public void dump(PrintStream p) {}

	@Override
	public void visitMethod(ASMParseContext context, MethodVisitor visitor) {}

	@Override
	public void run(EvaluationContext context) {}

	@Override
	public void prepareCompilation(ASMParseContext context) {}

	
}
