package org.stummi.evaluator.tools;

import org.stummi.evaluator.Evaluator;
import org.stummi.evaluator.SimpleEvaluator;
import org.stummi.evaluator.asm.ASMEvaluator;
import org.stummi.evaluator.exception.EvaluatorException;
import org.stummi.evaluator.exception.ExpressionSyntaxException;
import org.stummi.evaluator.expression.SingleVarExpression;

public class ExpressionRunner {
	public static void main(String[] args) throws EvaluatorException {
		if(args.length != 5) {
			usage();
		}
		
		Evaluator eval;
		if(args[0].equals("simple")) {
			eval = new SimpleEvaluator();
		} else if (args[0].equals("asm")) {
			eval = new ASMEvaluator();
		} else {
			usage();
			throw new IllegalStateException();
		}
		
		String exprStr = args[1];
		
		long t1 = System.currentTimeMillis();
		SingleVarExpression expr;
		try {
			expr = (SingleVarExpression) eval.parseExpression(exprStr);
		} catch (ExpressionSyntaxException ese) {
			System.err.println(ese.getMessage());
			ese.showPosition(System.err);
			
			System.exit(2);
			throw new IllegalStateException();
		}
		long t2 = System.currentTimeMillis();
		
		long compileTime = t2-t1;
		
		double xfrom = Double.parseDouble(args[2]);
		double xto = Double.parseDouble(args[3]);
		double xstep = Double.parseDouble(args[4]);
		
		if(xto <= xfrom || xstep <= 0) {
			usage();
		}

		int count = (int) ((xto-xfrom)/xstep);
		
		double[] input = new double[count];
		double[] result = new double[count];
		
		t1 = System.currentTimeMillis();
		for(int idx=0; idx<count; ++idx){
			double x = xfrom + xstep*idx;
			input[idx] = x;
			result[idx] = expr.run(x);
		}
		t2 = System.currentTimeMillis();
		
		for(int idx=0; idx<count; ++idx) {
			System.out.printf("%8.4f => %8.4f%n", input[idx], result[idx]);
		}
		
		System.out.println("---- SUMMARY FOR "+args[0].toUpperCase()+" EVALUATOR ----");
		System.out.println("Compile time: " + compileTime+ " ms");
		System.out.println("Executed expression " + count + " times in " + (t2-t1)+"ms");
	}

	private static void usage() {
		System.err.println("arguments: <simple|asm> <expression> <xfrom> <xto> <xstep>");
		System.exit(1);
	}
}
