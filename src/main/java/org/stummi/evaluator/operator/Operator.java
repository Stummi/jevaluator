package org.stummi.evaluator.operator;

import java.util.Stack;

import org.stummi.evaluator.Token;
import org.stummi.evaluator.instruction.Instruction;

public interface Operator extends Token {
	boolean isUnary();
	boolean isBinary();
	
	int getBinaryPrecedence();
	int getUnaryPrecedence();
	
	default Instruction getBinaryInstruction() {
		return c -> {
			Stack<Double> stack = c.getStack();
			Double right = stack.pop();
			Double left = stack.pop();
			stack.push(applyBinary(left, right));
		};
	}
	
	default Instruction getUnaryInstruction() {
		return c -> {
			Stack<Double> stack = c.getStack();
			stack.push(applyUnary(stack.pop()));
		};
	}
	
	Double applyUnary(Double right);
	Double applyBinary(Double left, Double right);
	
}
