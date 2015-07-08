package org.stummi.evaluator.operator;

import org.stummi.evaluator.Token;
import org.stummi.evaluator.instruction.Instruction;

public interface Operator extends Token {
	boolean isUnary();
	boolean isBinary();
	
	int getBinaryPrecedence();
	int getUnaryPrecedence();
	
	Instruction getBinaryInstruction();
	Instruction getUnaryInstruction();
	
}
