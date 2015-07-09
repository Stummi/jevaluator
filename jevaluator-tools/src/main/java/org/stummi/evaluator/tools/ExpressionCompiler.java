package org.stummi.evaluator.tools;

import java.io.FileOutputStream;
import java.io.IOException;

import org.stummi.evaluator.DefaultExpressionContext;
import org.stummi.evaluator.Evaluator;
import org.stummi.evaluator.ExpressionContext;
import org.stummi.evaluator.SimpleEvaluator;
import org.stummi.evaluator.asm.ASMClassLoader;
import org.stummi.evaluator.exception.ExpressionSyntaxException;
import org.stummi.evaluator.exception.ExpressionTreeException;
import org.stummi.evaluator.function.JavaFunction;
import org.stummi.evaluator.instruction.InstructionList;
import org.stummi.evaluator.operand.TokenGroup;
import org.stummi.evaluator.parser.Tokenizer;

public class ExpressionCompiler {
	public static void main(String[] args) throws ExpressionSyntaxException, ExpressionTreeException, IOException, ReflectiveOperationException {
		if(args.length != 2 && args.length != 3) {
			usage();
			System.exit(1);
		}
		
		String expression = args[0];
		String className = args[1];
		String fileName = args.length == 3 ? className + ".class" : args[2];
		
		Tokenizer tokenizer = new Tokenizer();
		TokenGroup tokens = tokenizer.tokenize(expression);
		Evaluator evaluator = new SimpleEvaluator();

		evaluator.getFunctionRegistry().registerFunction(new JavaFunction(Math.class, "sin", 1));

		ExpressionContext expressionContext = new DefaultExpressionContext();
		tokens.afterTokenizing(evaluator, expressionContext);
		InstructionList instruction = new InstructionList(tokens.operandInstruction());

		ASMClassLoader loader = new ASMClassLoader(ExpressionCompiler.class.getClassLoader());
		byte[] classCode = loader.compileInstructionList(instruction, className);

		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			fos.write(classCode);
		}

	}

	private static void usage() {
		System.out.println("arguments: <expression> <classname> [<filename>]");
	}
}
