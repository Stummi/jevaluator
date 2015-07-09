package org.stummi.evaluator.test;

import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import lombok.RequiredArgsConstructor;

import org.junit.Assert;
import org.junit.Test;
import org.stummi.evaluator.Evaluator;
import org.stummi.evaluator.exception.EvaluatorException;
import org.stummi.evaluator.expression.Expression;
import org.stummi.evaluator.expression.SingleVarExpression;
import org.stummi.evaluator.expression.StaticExpression;
import org.stummi.evaluator.function.JavaFunction;

@RequiredArgsConstructor
public abstract class AbstractEvaluatorTests {

	private final Evaluator evaluator;

	@Test
	public void testSimpleConstantExpressions() throws EvaluatorException {
		testStaticExpression("2", 2);
		testStaticExpression("2+3", 2 + 3);
		testStaticExpression("10/2", 10 / 2);
		testStaticExpression("10/3", 10 / 3D);
		testStaticExpression("5-3", 5 - 3);
		testStaticExpression("5 - + 3", 5 - +3);
		testStaticExpression("5 + - 3", 5 + -3);
		testStaticExpression("5 + - + - + - + - + - + + + - - - + + - - + - 3", 5 + -+-+-+-+-+ + +- - -+ +- -+-3);
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
		testStaticExpression("(2 + 23) + 42 / -3", (2 + 23) + 42 / -3);
	}

	@Test
	public void testVariables() throws EvaluatorException {
		testExpressionWithX("x", x -> x);
		testExpressionWithX("x * 2", x -> x * 2);
		testExpressionWithX("x + 2", x -> x + 2);
		testExpressionWithX("2 * (x + 1)", x -> 2 * (x + 1));
		testExpressionWithX("2 * -x", x -> 2 * -x);

		testExpressionWithXY("x * y", (x, y) -> x * y);
		testExpressionWithXY("x * (y + 1)", (x, y) -> x * (y + 1));

	}

	@Test
	public void testFunction() throws EvaluatorException, ReflectiveOperationException {
		evaluator.getFunctionRegistry().registerFunction(new JavaFunction(StrictMath.class, "sin", 1));
		evaluator.getFunctionRegistry().registerFunction(new JavaFunction(StrictMath.class, "cos", 1));
		evaluator.getFunctionRegistry().registerFunction(new JavaFunction(StrictMath.class, "tan", 1));
		evaluator.getFunctionRegistry().registerFunction(new JavaFunction(Math.class, "max", 2));
		evaluator.getFunctionRegistry().registerFunction(new JavaFunction(Math.class, "min", 2));

		testExpressionWithX("sin(x)", x -> Math.sin(x));
		testExpressionWithXY("sin(x+cos(y))+tan(y*y)", (x, y) -> sin(x + cos(y)) + tan(y * y));
		testExpressionWithXY("max(x,y) + min(cos(x), sin(y))", (x, y) -> max(x, y) + min(cos(x), sin(y)));
	}

	private void testExpressionWithX(String expression, DoubleUnaryOperator result) throws EvaluatorException {
		Expression exp = evaluator.parseExpression(expression);
		Assert.assertTrue("Expression should be an instance of an SingleVarExpression", exp instanceof SingleVarExpression);
		SingleVarExpression svarExpr = (SingleVarExpression)exp;
		
		for (double x = 0; x < 100; x += 0.1) {
			double expect = result.applyAsDouble(x);
			Assert.assertEquals(expect, svarExpr.run(Collections.singletonMap("x", x)), 1e-8);
			Assert.assertEquals(expect, svarExpr.run(x), 1e-8);
		}
	}

	private void testExpressionWithXY(String expression, DoubleBinaryOperator result) throws EvaluatorException {
		Expression exp = evaluator.parseExpression(expression);
		Map<String, Double> context = new HashMap<>();
		for (double x = 0; x < 100; x += 0.1) {
			context.put("x", x);
			for (double y = 0; y < 100; y += 0.1) {
				context.put("y", y);
				Assert.assertEquals(result.applyAsDouble(x, y), exp.run(context), 1e-8);
			}
		}
	}

	private void testStaticExpression(String expression, double result) throws EvaluatorException {
		Expression exp = evaluator.parseExpression(expression);
		Assert.assertTrue("Expression should be an instance of an StaticExpression", exp instanceof StaticExpression);
		StaticExpression staticExp = (StaticExpression)exp;
		Assert.assertEquals(result, staticExp.run(Collections.emptyMap()), 1e-8);
		Assert.assertEquals(result, staticExp.run(), 1e-8);
		Assert.assertEquals(result, evaluator.evaluate(expression), 1e-8);
	}
}
