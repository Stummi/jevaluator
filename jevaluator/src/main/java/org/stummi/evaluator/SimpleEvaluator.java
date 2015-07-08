package org.stummi.evaluator;

import java.util.ArrayList;
import java.util.List;

import org.stummi.evaluator.exception.EvaluatorException;
import org.stummi.evaluator.function.FunctionRegistry;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.InstructionList;
import org.stummi.evaluator.operand.TokenGroup;
import org.stummi.evaluator.parser.Tokenizer;

public class SimpleEvaluator implements Evaluator {
	private final FunctionRegistry functionRegistry = new FunctionRegistry();
	public SimpleEvaluator() {}

	@Override
	public Expression parseExpression(String expression) throws EvaluatorException {
		TokenGroup tokens = new Tokenizer().tokenize(expression);
		tokens.afterTokenizing(this);
		InstructionList list = flatList(tokens.operandInstruction());
		return instructionListToExpression(list);
	}
	
	protected Expression instructionListToExpression(InstructionList list) {
		return list;
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

	@Override
	public FunctionRegistry getFunctionRegistry() {
		return functionRegistry;
	}
	
}
