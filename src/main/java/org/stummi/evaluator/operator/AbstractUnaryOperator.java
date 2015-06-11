package org.stummi.evaluator.operator;


public abstract class AbstractUnaryOperator extends AbstractOperator {

	public AbstractUnaryOperator(int precedence) {
		super(true, false, precedence, -1);
	}
	
	@Override
	public Double applyBinary(Double left, Double right) {
		throw new UnsupportedOperationException(getClass().getSimpleName() + " cannot be applied binary");
	}

}
