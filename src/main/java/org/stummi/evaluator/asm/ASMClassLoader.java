package org.stummi.evaluator.asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.stummi.evaluator.Expression;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.InstructionList;

public class ASMClassLoader extends ClassLoader implements Opcodes {
	private AtomicInteger counter = new AtomicInteger();

	public ASMClassLoader(ClassLoader parent) {
		super(parent);
	}

	@SuppressWarnings("unchecked")
	Class<? extends Expression> classFromInstructionList(InstructionList list) {
		String name = "JITExpression$" + counter.incrementAndGet();
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		// writer.visit
		writer.visit(V1_6, ACC_PUBLIC, name, null, "java/lang/Object", new String[] { "org/stummi/evaluator/Expression" });
		MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(RETURN);

		mv.visitMaxs(0, 0);
		mv.visitEnd();
		mv = writer.visitMethod(ACC_PUBLIC, "run", "(Ljava/util/Map;)Ljava/lang/Double;",
				"(Ljava/util/Map<Ljava/lang/String;Ljava/lang/Double;>;)Ljava/lang/Double;", null);
		mv.visitCode();

		for (Instruction i : list.getInstructions()) {
			i.visitMethod(mv);
		}

		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
		writer.visitEnd();

		byte[] array = writer.toByteArray();
		try(FileOutputStream fout = new FileOutputStream("/tmp/jit/"+name+".class")) {
			fout.write(array);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Class<? extends Expression>) defineClass(name, array, 0, array.length);

	}
}
