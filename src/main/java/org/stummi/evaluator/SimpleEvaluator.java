package org.stummi.evaluator;

import java.util.ArrayList;
import java.util.List;

import org.stummi.evaluator.exception.EvaluatorException;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.InstructionList;
import org.stummi.evaluator.operand.TokenList;
import org.stummi.evaluator.parser.Tokenizer;

public class SimpleEvaluator implements Evaluator {
	public SimpleEvaluator() {}

	@Override
	public Expression parseExpression(String expression) throws EvaluatorException {
		TokenList list = new Tokenizer().tokenize(expression);
		return flatList(list.operandInstruction());
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
