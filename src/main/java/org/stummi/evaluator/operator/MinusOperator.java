package org.stummi.evaluator.operator;

public class MinusOperator extends AbstractOperator {

	public MinusOperator() {
		super(1, 3);
	}
	
	@Override
	public Double applyUnary(Double right) {
		return -right;
	}

	@Override
	public Double applyBinary(Double left, Double right) {
		return left-right;
	}

}
