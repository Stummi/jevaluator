package org.stummi.evaluator.instruction;

import java.io.PrintStream;

import lombok.RequiredArgsConstructor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.stummi.evaluator.EvaluationContext;
import org.stummi.evaluator.asm.ASMParseContext;

@RequiredArgsConstructor
public class PushVariableInstruction implements Instruction {
	private final String varName;

	@Override
	public void run(EvaluationContext context) {
		context.getStack().push(context.getEnvironment().get(varName));
	}

	@Override
	public void dump(PrintStream p) {
		p.println("push $" + varName);
	}

	@Override
	public void visitMethod(ASMParseContext context, MethodVisitor visitor) {
		int idx = context.getVariables().indexOf(varName);
		visitor.visitVarInsn(Opcodes.DLOAD, idx*2 + 1);
	}

	@Override
	public void prepareCompilation(ASMParseContext context) {
		if (!context.getVariables().contains(varName)) {
			context.addVariable(varName);
		}
	}
	
	

}
