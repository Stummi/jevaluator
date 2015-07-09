package org.stummi.evaluator.expression;

import java.util.Collections;


/**
 * Interface for an Expression with exactly no variables
 *  
 */
public interface StaticExpression extends Expression {
	default public Double run() {
		return run(Collections.emptyMap());
	}
}
