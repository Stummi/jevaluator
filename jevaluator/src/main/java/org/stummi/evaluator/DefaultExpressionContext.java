package org.stummi.evaluator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class DefaultExpressionContext implements ExpressionContext {
	@Getter
	private List<String> variables = new ArrayList<>();

	@Override
	public void addVariable(String variable) {
		if(!variables.contains(variable)) {
			variables.add(variable);
		}
		
	}

}
