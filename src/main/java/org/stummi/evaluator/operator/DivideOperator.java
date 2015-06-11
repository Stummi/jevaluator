package org.stummi.evaluator.operator;


public class DivideOperator extends AbstractBinaryOperator {

	public DivideOperator() {
		super(2);
	}

	@Override
	public Double applyBinary(Double left, Double right) {
		return left / right;
	}

}
