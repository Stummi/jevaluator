package org.stummi.evaluator;

import java.util.List;

public interface ExpressionContext {
	void addVariable(String variable);
	List<String> getVariables();
	
}
