package org.stummi.evaluator.parser;

import java.util.Stack;

import org.stummi.evaluator.Token;
import org.stummi.evaluator.exception.ExpressionSyntaxException;
import org.stummi.evaluator.operand.ConstantOperand;
import org.stummi.evaluator.operand.Operand;
import org.stummi.evaluator.operand.TokenList;
import org.stummi.evaluator.operand.VariableOperand;
import org.stummi.evaluator.operator.DivideOperator;
import org.stummi.evaluator.operator.MinusOperator;
import org.stummi.evaluator.operator.MultOperator;
import org.stummi.evaluator.operator.PlusOperator;

public class Tokenizer {
	private static final String OPERATORS = "()+-*/^";
	private Stack<TokenList> tokenStack = new Stack<>();
	private StringBuilder currentToken = new StringBuilder();
	private String currentExpression;
	private int currentTokenStart;
	private int currentPosition;

	public Tokenizer() {
	}

	public TokenList tokenize(String expression) throws ExpressionSyntaxException {
		tokenStack.clear();
		openGroup();
		currentToken = new StringBuilder();
		currentExpression = expression;
		currentPosition = 0;
		for (char c : expression.toCharArray()) {
			addChar(c);
			++currentPosition;
		}
		finishToken();
		
		if(tokenStack.size() == 1) {
			return tokenStack.pop();
		} else {
			tokenStack.clear();
			throw syntaxException("open group at end of expression");
		}
	}

	private void addChar(char c) throws ExpressionSyntaxException {
		if (c == ' ' || c == '\n' || c == '\t') {
			finishToken();
			return;
		}

		if(OPERATORS.indexOf(c) >= 0) {
			finishToken();
			addOperator(c);
			return;
		}
		
		if(currentToken.length() == 0) {
			currentTokenStart = currentPosition;
		}
		
		currentToken.append(c);
	}

	private void addOperator(char c) throws ExpressionSyntaxException {
		switch(c) {
		case '(':
			openGroup();
			break;
		case ')':
			closeGroup();
			break;
		case '+':
			addToken(new PlusOperator());
			break;
		case '*':
			addToken(new MultOperator());
			break;
		case '-':
			addToken(new MinusOperator());
			break;
		case '/':
			addToken(new DivideOperator());
			break;
		default:
			throw syntaxException("Unknown token " + c);
		}

	}
	
	private void addToken(Token t) {
		tokenStack.peek().addToken(t);
	}

	private void finishToken() throws ExpressionSyntaxException {
		if (currentToken.length() == 0) {
			return;
		}
		
		newToken(currentToken.toString());
		currentToken = new StringBuilder();
		currentTokenStart = currentPosition;
	}

	public void newToken(String token) throws ExpressionSyntaxException {
		if (token.isEmpty()) {
			return;
		}
		
		addString(token);
	}

	private void addString(String token) throws ExpressionSyntaxException {
		tokenStack.peek().addToken(operandFromString(token));
	}

	private Operand operandFromString(String token) throws ExpressionSyntaxException {
		if(Character.isDigit(token.charAt(0)) || token.charAt(0) == '.') {
			try {
				return new ConstantOperand(Double.parseDouble(token));
			} catch (NumberFormatException nfe) {
				throw syntaxException("Not a valid number: " + token);
			}
		} else {
			return new VariableOperand(token);
		}
	}

	private void closeGroup() throws ExpressionSyntaxException {
		TokenList t = tokenStack.pop();
		if(tokenStack.isEmpty()) {
			throw syntaxException("closing paranthesis without opening");
		}
		if(t.isEmpty()) {
			throw syntaxException("empty tokenset");
		}
		tokenStack.peek().addToken(t);
	}

	private ExpressionSyntaxException syntaxException(String message) {
		return new ExpressionSyntaxException(currentExpression, currentTokenStart, currentPosition, message);
	}

	private void openGroup() {
		tokenStack.add(new TokenList());
	}

}
