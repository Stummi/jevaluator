package org.stummi.evaluator.expression;


/**
 * Interface for an Expression with exactly one Variable.
 *  
 */
public interface SingleVarExpression extends Expression {
	public double run(double x);
}
