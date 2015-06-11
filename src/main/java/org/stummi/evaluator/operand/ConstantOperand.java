package org.stummi.evaluator.operand;

import lombok.RequiredArgsConstructor;

import org.stummi.evaluator.instruction.Instruction;

/**
 * {@link Operand} implementation which evaluates to a constant value
 *
 */
@RequiredArgsConstructor
public class ConstantOperand implements Operand {
	private final double val;

	@Override
	public Instruction operandInstruction() {
		return ctx -> ctx.getStack().push(val);
	}

}
