package org.stummi.evaluator.expression;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DelegatingSingleVarExpression implements SingleVarExpression {
	private final Expression delegate;
	private final String varname;

	@Override
	public Double run(Map<String, Double> environment) {
		return delegate.run(environment);
	}
	
	@Override
	public double run(double x) {
		return delegate.run(Collections.singletonMap(varname, x));
	}
	
	@Override
	public List<String> getRequiredVariables() {
		return Collections.singletonList(varname);
	}
	
	
}
