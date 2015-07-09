package org.stummi.evaluator.instruction;

import java.io.PrintStream;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.stummi.evaluator.EvaluationContext;
import org.stummi.evaluator.asm.ASMParseContext;
import org.stummi.evaluator.function.Function;

public class FunctionCallInstruction implements Instruction {


	private final boolean variadic;
	private final List<Instruction> arguments;
	private final Instruction instruction;
	public FunctionCallInstruction(Function function, List<Instruction> argumentInstructions) {
		if(!function.isVariadic() && function.argumentCount() != argumentInstructions.size()) {
			throw new IllegalArgumentException("argument list does not match function argument length");
		}
		
		this.variadic = function.isVariadic();
		this.arguments = argumentInstructions;
		this.instruction = function.instruction();
	}

	@Override
	public void run(EvaluationContext context) {
		arguments.forEach(i -> i.run(context));
		if(variadic) {
			new PushConstantInstruction(arguments.size()).run(context);
		}
		instruction.run(context);
	}

	@Override
	public void dump(PrintStream p) {
		arguments.forEach(i -> i.dump(p));
		if(variadic) {
			new PushConstantInstruction(arguments.size()).dump(p);
		}
		instruction.dump(p);
		
	}

	@Override
	public void visitMethod(ASMParseContext context, MethodVisitor visitor) {
		if(variadic) {
			addVariadicArguments(context, visitor);
		} else {
			addNonVariadicArguments(context, visitor);
		}
		instruction.visitMethod(context, visitor);
	}

	private void addNonVariadicArguments(ASMParseContext context, MethodVisitor visitor) {
		arguments.forEach(i -> i.visitMethod(context, visitor));
	}

	private void addVariadicArguments(ASMParseContext context, MethodVisitor visitor) {
		visitor.visitLdcInsn(arguments.size());
		visitor.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_DOUBLE);
		
		for(int idx=0; idx<arguments.size(); ++idx) {
			visitor.visitInsn(Opcodes.DUP);
			visitor.visitLdcInsn(idx);
			arguments.get(idx).visitMethod(context, visitor);
			visitor.visitInsn(Opcodes.DASTORE);
		}
	}

	@Override
	public void prepareCompilation(ASMParseContext context) {
		arguments.forEach(i -> i.prepareCompilation(context));
	}

}
