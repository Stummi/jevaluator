package org.stummi.evaluator.operand;

import lombok.RequiredArgsConstructor;

import org.stummi.evaluator.exception.ExpressionTreeException;
import org.stummi.evaluator.instruction.Instruction;

/**
 * {@link Operand} implementation which evaluates to a variable value, which
 * will be taken from the environment
 *
 */
@RequiredArgsConstructor
public class VariableOperand implements Operand {
	private final String variable;

	@Override
	public Instruction operandInstruction() throws ExpressionTreeException {
		return e -> e.getStack().push(e.getEnvironment().get(variable));
	}
}
