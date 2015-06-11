package org.stummi.evaluator.operand;

import org.stummi.evaluator.exception.ExpressionTreeException;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.InstructionList;
import org.stummi.evaluator.operator.Operator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenTree implements Operand {
	private final Operand lhs;
	private final Operator operator;
	private final Operand rhs;
	
	@Override
	public Instruction operandInstruction() throws ExpressionTreeException {
		if(lhs == null) {
			return new InstructionList(rhs.operandInstruction(), operator.getUnaryInstruction());
		} else {
			return new InstructionList(lhs.operandInstruction(), rhs.operandInstruction(), operator.getBinaryInstruction());	
		}
		
	}
	
}
