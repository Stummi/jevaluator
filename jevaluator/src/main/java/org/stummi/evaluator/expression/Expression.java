package org.stummi.evaluator.expression;

import java.util.List;
import java.util.Map;

/**
 * Interface for the compiled representation of a String expression. An
 * Expression can take an environment, providing any variables used in it and
 * return the evaluation result.
 */
public interface Expression {

	/**
	 * Runs the expression with the given Environment
	 * 
	 * @param environment
	 *            Map containing a value for all variables required by this
	 *            expression
	 * @return result of the Expression
	 */
	Double run(Map<String, Double> environment);
	
	/**
	 * Returns variables required by this expression to run
	 */
	List<String> getRequiredVariables();
}
