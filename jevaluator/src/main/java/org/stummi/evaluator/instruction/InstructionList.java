package org.stummi.evaluator.instruction;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.objectweb.asm.MethodVisitor;
import org.stummi.evaluator.EvaluationContext;
import org.stummi.evaluator.asm.ASMParseContext;
import org.stummi.evaluator.expression.Expression;

/**
 * Class Containing serval Instructions, exposing an {@link Instruction}
 * -Interface, which runs all Instructions within the {@link EvaluationContext}.
 * 
 * This class also contains the {@link Expression} interface and is meant to be
 * returned as Compiled Expression instance
 *
 */
@RequiredArgsConstructor
public class InstructionList implements Instruction {

	@Getter
	private final List<Instruction> instructions;

	
	public InstructionList(Instruction... instructions) {
		this.instructions = Arrays.asList(instructions);
	}

	@Override
	public void run(EvaluationContext context) {
		for (Instruction i : instructions) {
			i.run(context);
		}
	}

	@Override
	public void dump(PrintStream p) {
		instructions.forEach(i -> i.dump(p));
	}

	@Override
	public void visitMethod(ASMParseContext context, MethodVisitor visitor) {
		instructions.forEach(i -> i.visitMethod(context, visitor));
	}

	@Override
	public void prepareCompilation(ASMParseContext context) {
		instructions.forEach(i -> i.prepareCompilation(context));	
	}

}
