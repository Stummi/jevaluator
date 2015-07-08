package org.stummi.evaluator;

import java.util.Map;

/**
 * Interface for the compiled representation of a String expression. An
 * Expression can take an environment, providing any variables used in it and
 * return the evaluation result.
 */
public interface Expression {
	Double run(Map<String, Double> environment);
}
