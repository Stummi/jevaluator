package org.stummi.evaluator.operand;

import java.util.ArrayList;
import java.util.List;

import org.stummi.evaluator.Token;
import org.stummi.evaluator.exception.ExpressionTreeException;
import org.stummi.evaluator.instruction.Instruction;
import org.stummi.evaluator.instruction.NOPInstruction;
import org.stummi.evaluator.operator.Operator;

/**
 * A TokenList 
 */
public class TokenList implements Operand {

	private final List<Token> tokens;

	public TokenList(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	public TokenList() {
		this.tokens = new ArrayList<>();
	}
	
	public boolean isEmpty() {
		return tokens.isEmpty();
	}
	
	public void addToken(Token t) {
		tokens.add(t);
	}
	
	@Override
	public String toString() {
		return tokens.toString();
	}

	@Override
	public Instruction operandInstruction() throws ExpressionTreeException {
		if(tokens.size() == 0) {
			return new NOPInstruction();
		} else if(tokens.size() == 1) {
			return ((Operand)tokens.get(0)).operandInstruction();
		}
		
		return toTokenTree().operandInstruction();
	}

	public TokenTree toTokenTree() throws ExpressionTreeException {
		boolean hasLeftOperand = false;
		Operator operator = null;
		int currentPrecedence = Integer.MAX_VALUE;
		int operatorIdx = -2;
		
		
		for(int idx=0; idx<tokens.size(); ++idx) {
			Token t = tokens.get(idx);
			if(t instanceof Operand) {
				if(hasLeftOperand) {
					throw new ExpressionTreeException("two operands");
				}
				//leftOperand = (Operand)t;
				hasLeftOperand = true;
				continue;
			}
			
			if(t instanceof Operator && tokens.get(idx + 1) instanceof Operand) {
				Operator op = (Operator)t;
				int precedence;
				
				if(!hasLeftOperand) {
					if(!op.isUnary()) {
						throw new ExpressionTreeException(op.getClass().getSimpleName() + " cannot be used unary");	
					}
					precedence = op.getUnaryPrecedence();
				} else {
					if(!op.isBinary()) {
						throw new ExpressionTreeException(op.getClass().getSimpleName() + " cannot be used unary");
					}
					precedence = op.getBinaryPrecedence();
				}
				
				if(precedence < currentPrecedence) {
					currentPrecedence = precedence;
					operator = op;
					operatorIdx = idx;
				}
				
				hasLeftOperand = false;
				
				
			}
		}
		
		List<Token> leftList, rightList;
		TokenTree tree;
		Operand right = (Operand)tokens.get(operatorIdx+1);
		Operand left;
		if(operatorIdx > 0 && tokens.get(operatorIdx-1) instanceof Operand) {
			left = (Operand) tokens.get(operatorIdx -1);
		} else {
			left = null;
		}
		if(left == null) {
			leftList = tokens.subList(0, operatorIdx);
			tree = new TokenTree(null, operator, right);
		} else {
			leftList = tokens.subList(0, operatorIdx-1);
			tree = new TokenTree(left, operator, right);
		}
		rightList = tokens.subList(operatorIdx+2, tokens.size());

		List<Token> list = new ArrayList<>();
		if(leftList.isEmpty() && rightList.isEmpty()) {
			return tree;
		}
		
		list.addAll(leftList);
		list.add(tree);
		list.addAll(rightList);
		return new TokenList(list).toTokenTree();
	};
	
}
