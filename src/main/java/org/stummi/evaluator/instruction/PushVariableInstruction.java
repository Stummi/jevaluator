package org.stummi.evaluator.instruction;

import java.io.PrintStream;

import lombok.RequiredArgsConstructor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.stummi.evaluator.EvaluationContext;

@RequiredArgsConstructor
public class PushVariableInstruction implements Instruction {
	private final String varName;
	
	@Override
	public void run(EvaluationContext context) {
		context.getStack().push(context.getEnvironment().get(varName));
	}

	@Override
	public void dump(PrintStream p) {
		p.println("push $"+varName);
	}

	@Override
	public void visitMethod(MethodVisitor visitor) {
		visitor.visitVarInsn(Opcodes.ALOAD, 1);
		visitor.visitLdcInsn(varName);
		visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
		visitor.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
		visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
	}

}
