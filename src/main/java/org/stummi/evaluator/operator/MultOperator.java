package org.stummi.evaluator.operator;


public class MultOperator extends AbstractBinaryOperator {

	public MultOperator() {
		super(2);
	}

	@Override
	public Double applyBinary(Double left, Double right) {
		return left * right;
	}

}
