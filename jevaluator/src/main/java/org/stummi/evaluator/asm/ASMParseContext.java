package org.stummi.evaluator.asm;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ASMParseContext {
	private final List<String> variables = new ArrayList<>();
	
	public void addVariable(String variable) {
		variables.add(variable);
	}
	
}
