package org.stummi.evaluator.function;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Stack;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.stummi.evaluator.EvaluationContext;
import org.stummi.evaluator.asm.ASMParseContext;
import org.stummi.evaluator.instruction.Instruction;

public class JavaFunction implements Function, Instruction {
	private final Method method;
	private final String name;
	private final int argumentCount;

	public JavaFunction(Class<?> container, String methodName, int argumentCount) throws ReflectiveOperationException {
		
		Class<?>[] arg;
		
		if(argumentCount >= 0) {
		arg = new Class<?>[argumentCount];
		Arrays.fill(arg, double.class);
		} else {
			argumentCount = -1;
			arg = new Class<?>[] { double[].class };
		}
		try {
			this.method = container.getMethod(methodName, arg);
			if((method.getModifiers() & Modifier.STATIC) == 0) {
				throw new IllegalArgumentException("can only take static methods");
			}
		} catch (NoSuchMethodException | SecurityException e) {	
			throw e;
		}
		
		this.name = methodName;
		this.argumentCount = argumentCount;
		
	}
	
	@Override
	public int argumentCount() {
		return argumentCount;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Instruction instruction() {
		return this;
	}

	@Override
	public void run(EvaluationContext context) {
		if(isVariadic()) {
			runVariadic(context);
			return;
		} 
		
		Stack<Double> stack = context.getStack();
		Object[] d = new Object[argumentCount];
		for(int idx=d.length-1; idx>=0; --idx) {
			d[idx] = stack.pop();
		}
		
		try {
			double ret = ((Number)method.invoke(null, d)).doubleValue();
			stack.push(ret);
		} catch (ReflectiveOperationException re) {
			throw new RuntimeException(re);
		}
	}

	private void runVariadic(EvaluationContext context) {
		Stack<Double> stack = context.getStack();
		int count = stack.pop().intValue();
		double[] d = new double[count];
		for(int idx=d.length-1; idx>=0; --idx) {
			d[idx] = stack.pop();
		}
		
		try {
			double ret = ((Number)method.invoke(null, d)).doubleValue();
			stack.push(ret);
		} catch (ReflectiveOperationException re) {
			throw new RuntimeException(re);
		}
	}

	@Override
	public void dump(PrintStream p) {
		if(isVariadic())
			p.println("Call: " + name + "(...)");
		else
			p.println("Call: " + name + "("+argumentCount+ " arguments)");
	}

	@Override
	public void visitMethod(ASMParseContext context, MethodVisitor visitor) {
		String owner = method.getDeclaringClass().getCanonicalName().replace('.', '/');
		StringBuilder signature = new StringBuilder("(");
		if(isVariadic()) {
			signature.append("[D");
		} else {
			for(int idx=0; idx<argumentCount; ++idx) {
				signature.append("D");
			}
		}
		signature.append(")D");
		visitor.visitMethodInsn(Opcodes.INVOKESTATIC, owner, name, signature.toString(), false);
	}

	@Override
	public void prepareCompilation(ASMParseContext context) {}

}
