package org.stummi.evaluator.test;

import java.util.Collections;
import java.util.function.DoubleUnaryOperator;

import org.junit.Assert;
import org.junit.Test;
import org.stummi.evaluator.Expression;
import org.stummi.evaluator.SimpleEvaluator;
import org.stummi.evaluator.exception.EvaluatorException;

public class EvaluatorTests {

	private SimpleEvaluator evaluator;

	public EvaluatorTests() {
		this.evaluator = new SimpleEvaluator();
	}

	@Test
	public void testSimpleConstantExpressions() throws EvaluatorException {
		testStaticExpression("2", 2);
		testStaticExpression("2+3", 2 + 3);
		testStaticExpression("10/2", 10 / 2);
		testStaticExpression("10/3", 10 / 3D);
		testStaticExpression("5-3", 5 - 3);
		testStaticExpression("5 - + 3", 5 - +3);
		testStaticExpression("5 + - 3", 5 + -3);
		testStaticExpression("5 + - + - + - + - + - + + + - - - + + - - + - 3", 5 + - + - + - + - + - + + + - - - + + - - + - 3);
		testStaticExpression("5 - - 3", 5 - -3);
		testStaticExpression("5 - - 3", 5 - -3);
		testStaticExpression("5 - - - 3", 5 - - -3);
	}
	
	@Test
	public void testPrecedences() throws EvaluatorException {
		testStaticExpression("2 * 3 + 4", 2 * 3 + 4);
		testStaticExpression("2 + 3 * 4", 2 + 3 * 4);
		testStaticExpression("(2+3) * 4", (2 + 3) * 4);
		testStaticExpression("-(2+3) * (4+5)", -(2 + 3) * (4 + 5));
	}
	
	@Test
	public void testVariables() throws EvaluatorException {
		testExpressionWithX("x", x -> x);
		testExpressionWithX("x * 2", x -> x*2);
		testExpressionWithX("x + 2", x -> x+2);
		testExpressionWithX("2*(x+1)", x -> 2*(x+1));
		testExpressionWithX("2*-x", x -> 2*-x);
	}

	private void testExpressionWithX(String expression, DoubleUnaryOperator result) throws EvaluatorException {
		Expression exp = evaluator.parseExpression(expression);
		for(double x = 0; x<100; x+=0.1) {
			Assert.assertEquals(result.applyAsDouble(x), exp.run(Collections.singletonMap("x", x)), 1e-8);
		}
	}
	
	private void testStaticExpression(String expression, double result) throws EvaluatorException {
		Assert.assertEquals(result, evaluator.evaluate(expression), 1e-8);
	}
}
