package org.stummi.evaluator;

import java.util.Collections;
import java.util.Map;

import org.stummi.evaluator.exception.EvaluatorException;
import org.stummi.evaluator.function.FunctionRegistry;

/**
 * Interface for the actual Evaluator which is intented to be used by the User.
 * 
 * The evaluator can compile an String into an {@link Expression} instance,
 * which can be evaluated with a context
 * 
 * The implementation may or may not cache identical expressions for
 * performance, so please consult the implementations documentation as well
 */
public interface Evaluator {
	/**
	 * Compiles an String represented expression and returns the
	 * {@link Expression} instance
	 *
	 * @param expression
	 * @return
	 * @throws EvaluatorException
	 *             it the expression could not be parsed
	 */
	Expression parseExpression(String expression) throws EvaluatorException;

	FunctionRegistry getFunctionRegistry();
	
	/**
	 * Shortcut method which compiles and runs the expression with an empty environment
	 */
	default Double evaluate(String expression) throws EvaluatorException {
		return evaluate(expression, Collections.emptyMap());
	}

	/**
	 * Shortcut method which compiles and runs the expression with the given environment
	 */
	default Double evaluate(String expression, Map<String, Double> environment) throws EvaluatorException {
		return parseExpression(expression).run(environment);
	}
}
