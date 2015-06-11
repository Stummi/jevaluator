package org.stummi.evaluator.test;

import org.junit.Assert;
import org.junit.Test;
import org.stummi.evaluator.SimpleEvaluator;
import org.stummi.evaluator.exception.EvaluatorException;

public class EvaluatorTests {

	private SimpleEvaluator evaluator;

	public EvaluatorTests() {
		this.evaluator = new SimpleEvaluator();
	}

	@Test
	public void testSimpleExpressions() throws EvaluatorException {
		testStaticExpression("2", 2);
		testStaticExpression("2+3", 2 + 3);
		testStaticExpression("10/2", 10 / 2);
		testStaticExpression("10/3", 10 / 3D);
		testStaticExpression("5-3", 5 - 3);
		testStaticExpression("5 + - 3", 5 + -3);
		testStaticExpression("5 - - 3", 5 - -3);
		testStaticExpression("5 - - 3", 5 - -3);
		testStaticExpression("5 - - - 3", 5 - - - 3);
	}

	private void testStaticExpression(String expression, double result) throws EvaluatorException {
		Assert.assertEquals(result, evaluator.evaluate(expression), 1e-8);
	}
}
