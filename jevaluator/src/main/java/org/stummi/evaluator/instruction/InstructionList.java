package org.stummi.evaluator.instruction;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
public class InstructionList implements Instruction, Expression {

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
	public Double run(Map<String, Double> environment) {
		EvaluationContext context = new EvaluationContext(environment);
		run(context);
		Stack<Double> s = context.getStack();

		if (s.size() != 1) {
			throw new IllegalStateException("Expected a stack size from 1 at end but got " + s.size());
		}

		return s.pop();

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
