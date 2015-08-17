package org.stummi.evaluator.asm;

import org.stummi.evaluator.ExpressionContext;
import org.stummi.evaluator.SimpleEvaluator;
import org.stummi.evaluator.expression.Expression;
import org.stummi.evaluator.instruction.InstructionList;

public class ASMEvaluator extends SimpleEvaluator {
	private final ASMClassLoader loader;

	public ASMEvaluator() {
		this.loader = new ASMClassLoader();
	}

	@Override
	protected Expression instructionListToExpression(InstructionList list, ExpressionContext context) {
		try {
			return loader.createExpressionClass(list).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
