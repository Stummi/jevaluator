package org.stummi.evaluator;

import java.util.Map;
import java.util.Stack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EvaluationContext {
	private final Stack<Double> stack = new Stack<>();
	private final Map<String, Double> environment;
}
