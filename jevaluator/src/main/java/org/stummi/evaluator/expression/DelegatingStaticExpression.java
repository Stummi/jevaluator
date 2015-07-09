package org.stummi.evaluator.expression;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DelegatingStaticExpression implements StaticExpression {
	private final Expression delegate;
	
	@Override
	public Double run(Map<String, Double> environment) {
		return delegate.run(environment);
	}

}
