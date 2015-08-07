package org.stummi.evaluator.asm;

import org.stummi.evaluator.ExpressionContext;
import org.stummi.evaluator.SimpleEvaluator;
import org.stummi.evaluator.expression.Expression;
import org.stummi.evaluator.instruction.InstructionList;

public class ASMEvaluator extends SimpleEvaluator {
	private final ASMClassLoader loader;

	public ASMEvaluator() {
		this.loader = new ASMClassLoader(parent());
	}
	
	private static ClassLoader parent() {
		ClassLoader myLoader = ASMClassLoader.class.getClassLoader();
		return myLoader == null ? ClassLoader.getSystemClassLoader() : myLoader;
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
