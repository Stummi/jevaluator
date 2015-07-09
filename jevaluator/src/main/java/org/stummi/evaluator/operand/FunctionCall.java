package org.stummi.evaluator.operand;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.stummi.evaluator.EvaluatorContext;
import org.stummi.evaluator.ExpressionContext;
import org.stummi.evaluator.exception.ExpressionTreeException;
import org.stummi.evaluator.function.Function;
import org.stummi.evaluator.instruction.FunctionCallInstruction;
import org.stummi.evaluator.instruction.Instruction;

@RequiredArgsConstructor
public class FunctionCall implements Operand {
	private final String functionName;
	private final TokenList arguments;
	private Function function;
	
	@Override
	public Instruction operandInstruction() throws ExpressionTreeException {
		if(function == null) {
			throw new NullPointerException("unresolved method: " + functionName + " with " + arguments.size() + " args");
		}

		List<Instruction> argumentInstructions = new ArrayList<>();
		for(TokenGroup tg:arguments.getGroups()) {
			argumentInstructions.add(tg.operandInstruction());
		}
		
		return new FunctionCallInstruction(function, argumentInstructions);

	}

	@Override
	public void afterTokenizing(EvaluatorContext evaluator, ExpressionContext expressionContext) {
		int argCount = arguments.size();
		function = evaluator.getFunctionRegistry().getFunction(functionName, argCount);
		
		arguments.afterTokenizing(evaluator, expressionContext);
	}
}
