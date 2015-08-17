package org.stummi.evaluator.asm;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.stummi.evaluator.expression.Expression;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.InstructionList;

public class ASMClassLoader extends ClassLoader implements Opcodes {
	private AtomicInteger counter = new AtomicInteger();
	
	public ASMClassLoader() {
		super(parent());
	}
	
	public ASMClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	private static ClassLoader parent() {
		ClassLoader myLoader = ASMClassLoader.class.getClassLoader();
		return myLoader == null ? ClassLoader.getSystemClassLoader() : myLoader;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Expression> createExpressionClass(InstructionList list) {
		String name = "JITExpression$" + counter.incrementAndGet();
		byte[] data = compileInstructionList(list, name);
		return (Class<? extends Expression>) defineClass(name, data, 0, data.length);
	}
	
	public byte[] compileInstructionList(InstructionList list, String name) {
		ASMParseContext parserContext = new ASMParseContext();
		list.getInstructions().forEach(i -> i.prepareCompilation(parserContext));
		
		final String ifaceName;
		switch (parserContext.getVariables().size()) {
		case 0:
			ifaceName = "StaticExpression";
			break;
		case 1:
			ifaceName = "SingleVarExpression";
			break;
		default:
			ifaceName = "Expression";
			break;
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		
		// write class header
		writer.visit(V1_5, ACC_PUBLIC, name, null, "java/lang/Object", new String[] { "org/stummi/evaluator/expression/"+ifaceName });
		
		// Write Constructor
		MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
		
		
		// write internal run method
		StringBuilder sb = new StringBuilder("(");
		for(int idx=0; idx<parserContext.getVariables().size(); ++idx) {
			sb.append("D");
		}
		sb.append(")D");
		mv = writer.visitMethod(ACC_PUBLIC, "run", sb.toString(), null, null);

		Label start = new Label();
		Label stop = new Label();
		mv.visitLabel(start);
		for (Instruction i : list.getInstructions()) {
			i.visitMethod(parserContext, mv);
		}
		mv.visitLabel(stop);
		
		int slot = 1;
		for(String s : parserContext.getVariables()) {
			mv.visitLocalVariable(s, "D", null, start, stop, slot);
			slot += 2;
		}

		mv.visitInsn(DRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
		
		
		// write Expression interface run method
		mv = writer.visitMethod(ACC_PUBLIC, "run", "(Ljava/util/Map;)Ljava/lang/Double;",
				"(Ljava/util/Map<Ljava/lang/String;Ljava/lang/Double;>;)Ljava/lang/Double;", null);
		mv.visitCode();

		mv.visitVarInsn(Opcodes.ALOAD, 0);
		for(String var:parserContext.getVariables()) {
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitLdcInsn(var);
			mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
		}
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, name, "run", sb.toString(), false);
		
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
		
		
		// write Expression interface getVariables method
		List<String> variables = parserContext.getVariables();
		mv = writer.visitMethod(ACC_PUBLIC, "getRequiredVariables", "()Ljava/util/List;", "()Ljava/util/List<Ljava/langString;>;", null);
		mv.visitLdcInsn(variables.size());
		mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
		
		for(int idx=0; idx<variables.size(); ++idx) {
			mv.visitInsn(DUP);
			mv.visitLdcInsn(idx);
			mv.visitLdcInsn(variables.get(idx));
			mv.visitInsn(AASTORE);
		}
		
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Arrays", "asList", "([Ljava/lang/Object;)Ljava/util/List;", false);
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
		
		writer.visitEnd();

		return writer.toByteArray();
	}
}
