package org.stummi.evaluator.instruction;

import java.util.Stack;

import org.stummi.evaluator.EvaluationContext;

public abstract class AbstractUnaryInstruction extends AbstractOperatorInstruction {

	public AbstractUnaryInstruction(String name, int opcode) {
		super(name, opcode);
	}

	@Override
	public void run(EvaluationContext context) {
		Stack<Double> stack = context.getStack();
		Double right = stack.pop();
		stack.push(apply(right));
	}

	protected abstract Double apply(Double right);

}
