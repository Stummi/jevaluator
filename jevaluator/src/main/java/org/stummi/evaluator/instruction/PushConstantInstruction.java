package org.stummi.evaluator.instruction;

import java.io.PrintStream;

import lombok.RequiredArgsConstructor;

import org.objectweb.asm.MethodVisitor;
import org.stummi.evaluator.EvaluationContext;
import org.stummi.evaluator.asm.ASMParseContext;

@RequiredArgsConstructor
public class PushConstantInstruction implements Instruction {
	private final double val;
	
	@Override
	public void run(EvaluationContext context) {
		context.getStack().add(val);
	}

	@Override
	public void dump(PrintStream p) {
		p.println("push " + val);
	}

	@Override
	public void visitMethod(ASMParseContext context, MethodVisitor visitor) {
		visitor.visitLdcInsn(val);
	}

	@Override
	public void prepareCompilation(ASMParseContext context) {}

}
