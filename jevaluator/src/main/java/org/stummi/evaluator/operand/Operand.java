package org.stummi.evaluator.operand;

import org.stummi.evaluator.Token;
import org.stummi.evaluator.exception.ExpressionTreeException;
import org.stummi.evaluator.instruction.Instruction;

/**
 * An Operand is a value - or anything which can be evaluated to a value - within an expression
 */
public interface Operand extends Token {
	Instruction operandInstruction() throws ExpressionTreeException;
}
