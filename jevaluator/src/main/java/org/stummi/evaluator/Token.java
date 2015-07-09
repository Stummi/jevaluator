package org.stummi.evaluator;

/**
 * Superinterface for all Parts an Expression consists of. 
 * 
 * Opening an
 */
public interface Token {
	default void afterTokenizing(EvaluatorContext evaluator, ExpressionContext expressionContext) {}
}
