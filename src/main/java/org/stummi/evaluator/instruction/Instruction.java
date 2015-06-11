package org.stummi.evaluator.instruction;

import java.io.PrintStream;

import org.objectweb.asm.MethodVisitor;
import org.stummi.evaluator.EvaluationContext;

/**
 * An Instructions operates on an {@link EvaluationContext}, which consists of
 * the Stack and the Environment. An compiled Expression is a list of Instructions (see {@link InstructionList}
 */
public interface Instruction {
	void run(EvaluationContext context);
	void dump(PrintStream p);
	void visitMethod(MethodVisitor visitor);
}
