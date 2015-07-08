package org.stummi.evaluator.function;

import java.util.HashMap;
import java.util.Map;

import lombok.Value;

public class FunctionRegistry {
	@Value
	private static class Signature {
		String name;
		int argCount;
		
		public boolean isVariadic() {
			return argCount == -1;
		}

		public Signature asVariadic() {
			return new Signature(name, -1);
		}
	}
	
	private final Map<Signature, Function> functionMap = new HashMap<>();
	
	public void registerFunction(Function f) {
		Signature sig = new Signature(f.name(), f.argumentCount());
		if(functionMap.containsKey(sig)) {
			throw new IllegalStateException("Function with signature alread registered: " + sig);
		}
		functionMap.put(sig, f);
	}
	
	public Function getFunction(String name, int argCount) {
		Signature sig = new Signature(name, argCount);
		Function f = functionMap.get(sig);
		if(f == null && !sig.isVariadic()) {
			f = functionMap.get(sig.asVariadic());
		}
		
		return f;
	}
}
