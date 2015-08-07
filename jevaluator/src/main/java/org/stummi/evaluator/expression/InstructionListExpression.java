package org.stummi.evaluator.expression;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import lombok.RequiredArgsConstructor;

import org.stummi.evaluator.EvaluationContext;
import org.stummi.evaluator.instruction.InstructionList;

@RequiredArgsConstructor
public class InstructionListExpression implements Expression {

	private final InstructionList instructionList;
	private final List<String> requiredVariables;
	
	@Override
	public Double run(Map<String, Double> environment) {
		EvaluationContext context = new EvaluationContext(environment);
		instructionList.run(context);
		Stack<Double> s = context.getStack();

		if (s.size() != 1) {
			throw new IllegalStateException("Expected a stack size from 1 at end but got " + s.size());
		}

		return s.pop();

	}

	@Override
	public List<String> getRequiredVariables() {
		return requiredVariables;
	}

}
