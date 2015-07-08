package org.stummi.evaluator.operand;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.stummi.evaluator.exception.ExpressionTreeException;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.PushVariableInstruction;

/**
 * {@link Operand} implementation which evaluates to a variable value, which
 * will be taken from the environment
 *
 */
@RequiredArgsConstructor
@Getter
public class VariableOperand implements Operand {
	private final String variable;

	@Override
	public Instruction operandInstruction() throws ExpressionTreeException {
		return new PushVariableInstruction(variable);
	}
}
