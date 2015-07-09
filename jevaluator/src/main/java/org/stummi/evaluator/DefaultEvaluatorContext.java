package org.stummi.evaluator;

import lombok.Getter;

import org.stummi.evaluator.function.FunctionRegistry;

@Getter
public class DefaultEvaluatorContext implements EvaluatorContext {
	private final FunctionRegistry functionRegistry = new FunctionRegistry();
}
