package org.stummi.evaluator.instruction;

import java.util.Stack;

import org.stummi.evaluator.EvaluationContext;

public abstract class AbstractBinaryInstruction extends AbstractOperatorInstruction {


	public AbstractBinaryInstruction(String name, int opcode) {
		super(name, opcode);
	}

	@Override
	public void run(EvaluationContext context) {
		Stack<Double> stack = context.getStack();
		Double right = stack.pop();
		Double left = stack.pop();
		stack.push(apply(left, right));
	}

	protected abstract Double apply(Double left, Double right);
	
}
