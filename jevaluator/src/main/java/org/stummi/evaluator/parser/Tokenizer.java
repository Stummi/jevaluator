package org.stummi.evaluator.parser;

import java.util.Stack;

import org.stummi.evaluator.Token;
import org.stummi.evaluator.exception.ExpressionSyntaxException;
import org.stummi.evaluator.exception.ExpressionTreeException;
import org.stummi.evaluator.operand.ConstantOperand;
import org.stummi.evaluator.operand.FunctionCall;
import org.stummi.evaluator.operand.Operand;
import org.stummi.evaluator.operand.TokenGroup;
import org.stummi.evaluator.operand.TokenList;
import org.stummi.evaluator.operand.VariableOperand;
import org.stummi.evaluator.operator.DivideOperator;
import org.stummi.evaluator.operator.MinusOperator;
import org.stummi.evaluator.operator.MultOperator;
import org.stummi.evaluator.operator.PlusOperator;

public class Tokenizer {
	private static final String OPERATORS = "()+-*/^,";
	private Stack<TokenList> tokenStack = new Stack<>();
	private StringBuilder currentToken = new StringBuilder();
	private String currentExpression;
	private int currentTokenStart;
	private int currentPosition;

	public Tokenizer() {
	}

	public TokenGroup tokenize(String expression)
			throws ExpressionSyntaxException {
		tokenStack.clear();
		openList();
		currentToken = new StringBuilder();
		currentExpression = expression;
		currentPosition = 0;
		for (char c : expression.toCharArray()) {
			addChar(c);
			++currentPosition;
		}
		finishToken();
		
		if (tokenStack.size() == 1) {
			TokenList tokenList = tokenStack.pop();
			if(tokenList.isEmpty()) {
				throw syntaxException("empty expression");
			}
			return tokenList.getSingleGroup();
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

		if (OPERATORS.indexOf(c) >= 0) {
			finishToken();
			addOperator(c);
			return;
		}

		if (currentToken.length() == 0) {
			currentTokenStart = currentPosition;
		}

		currentToken.append(c);
	}

	private void addOperator(char c) throws ExpressionSyntaxException {
		switch (c) {
		case '(':
			openList();
			break;
		case ')':
			closeList();
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
		case ',':
			addComma();
			break;
		default:
			throw syntaxException("Unknown token " + c);
		}

	}

	private void addComma() {
		try {
			tokenStack.peek().newTokenGroup();
		} catch (ExpressionTreeException ete) {
			syntaxException(ete.getMessage());
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

	private Operand operandFromString(String token)
			throws ExpressionSyntaxException {
		if (Character.isDigit(token.charAt(0)) || token.charAt(0) == '.') {
			try {
				return new ConstantOperand(Double.parseDouble(token));
			} catch (NumberFormatException nfe) {
				throw syntaxException("Not a valid number: " + token);
			}
		} else {
			return new VariableOperand(token);
		}
	}

	private void closeList() throws ExpressionSyntaxException {
		TokenList t = tokenStack.pop();
		if (tokenStack.isEmpty()) {
			throw syntaxException("closing paranthesis without opening");
		}

		TokenGroup lastGroup = tokenStack.peek().getOrCreateCurrentGroup();
		Token lastToken = lastGroup.isEmpty() ? null : lastGroup.getLastToken();
		
		if (lastToken instanceof VariableOperand) {
			lastGroup.removeLastToken();
			lastGroup.addToken(new FunctionCall(((VariableOperand) lastToken)
					.getVariable(), t));
		} else {
			lastGroup.addToken(t.getSingleGroup());
		}
	}

	private ExpressionSyntaxException syntaxException(String message) {
		return new ExpressionSyntaxException(currentExpression,
				currentTokenStart, currentPosition, message);
	}

	private void openList() {
		tokenStack.add(new TokenList());
	}

}
