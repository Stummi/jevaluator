package org.stummi.evaluator.operator;


public abstract class AbstractBinaryOperator extends AbstractOperator {

	public AbstractBinaryOperator(int precedence) {
		super(false, true, -1, precedence);
	}
	
	@Override
	public Double applyUnary(Double right) {
		throw new UnsupportedOperationException(getClass().getSimpleName() + " cannot be applied unary");
	}

}
