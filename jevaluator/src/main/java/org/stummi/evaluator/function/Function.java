package org.stummi.evaluator.function;

import org.stummi.evaluator.instruction.Instruction;

/**
 * @author stummi
 *
 */
public interface Function {
	default boolean isVariadic() {
		return argumentCount() == -1;
	}
	
	/**
	 * returns the number of arguments this function takes, or -1 if variadic
	 */
	int argumentCount();

	/**
	 * returns the name of the function
	 */
	String name();

	/**
	 * returns the instruction to invoke the function, with the following
	 * asumptions: If the function has arguments and is non-variadic, all
	 * Arguments needs to be on stack have all its arguments on the stack. Last
	 * argument on top.
	 * 
	 * If its a variadic function, the number of arguments should lay on stack,
	 * than all arguments, with the last on top. In ASM-Implementation, an array
	 * should lay on stack
	 */
	Instruction instruction();
}
