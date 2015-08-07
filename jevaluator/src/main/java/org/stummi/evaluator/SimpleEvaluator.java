package org.stummi.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.stummi.evaluator.exception.EvaluatorException;
import org.stummi.evaluator.expression.DelegatingSingleVarExpression;
import org.stummi.evaluator.expression.DelegatingStaticExpression;
import org.stummi.evaluator.expression.Expression;
import org.stummi.evaluator.expression.InstructionListExpression;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.InstructionList;
import org.stummi.evaluator.operand.TokenGroup;
import org.stummi.evaluator.parser.Tokenizer;

public class SimpleEvaluator extends DefaultEvaluatorContext implements Evaluator {
	public SimpleEvaluator() {}

	@Override
	public Expression parseExpression(String expression) throws EvaluatorException {
		ExpressionContext context = new DefaultExpressionContext();
		TokenGroup tokens = new Tokenizer().tokenize(expression);
		tokens.afterTokenizing(this, context);
		InstructionList list = flatList(tokens.operandInstruction());
		return instructionListToExpression(list, context);
	}
	
	protected Expression instructionListToExpression(InstructionList list, ExpressionContext context) {
		Expression expr = new InstructionListExpression(list, Collections.unmodifiableList(context.getVariables()));
		switch(context.getVariables().size()) {
		case 0:
			return new DelegatingStaticExpression(expr);
		case 1:
			return new DelegatingSingleVarExpression(expr, context.getVariables().get(0));
		default:
			return expr;
		}
	}

	private static InstructionList flatList(Instruction i) {
		List<Instruction> iList = new ArrayList<>();
		flatList(i, iList);
		return new InstructionList(iList);
	}

	private static void flatList(Instruction inst, List<Instruction> iList) {
		if(inst instanceof InstructionList) {
			for(Instruction i : ((InstructionList)inst).getInstructions()) {
				flatList(i, iList);
			}
		} else {
			iList.add(inst);
		}
	}

	
}
