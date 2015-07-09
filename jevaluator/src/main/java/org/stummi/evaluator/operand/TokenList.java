package org.stummi.evaluator.operand;

import java.util.ArrayList;
import java.util.List;

import org.stummi.evaluator.EvaluatorContext;
import org.stummi.evaluator.ExpressionContext;
import org.stummi.evaluator.Token;
import org.stummi.evaluator.exception.ExpressionTreeException;

public class TokenList implements Token {
	private final List<TokenGroup> tokens = new ArrayList<>();
	
	public TokenGroup getSingleGroup() {
		if(tokens.size() == 1) {
			return tokens.get(0);
		} else {
			throw new IllegalStateException("Expected exactly one Token");
		}
	}

	public void newTokenGroup() throws ExpressionTreeException {
		if(!tokens.isEmpty() && getCurrentGroup().isEmpty()) {
			throw new ExpressionTreeException("Empty TokenGrop");
		}
		
		tokens.add(new TokenGroup());
	}

	public TokenGroup getOrCreateCurrentGroup() {
		if(tokens.isEmpty()) {
			tokens.add(new TokenGroup());
		}
		return getCurrentGroup();
	}
	
	public TokenGroup getCurrentGroup() {
		return tokens.get(tokens.size()-1);
	}

	public void addToken(Token t) {
		getOrCreateCurrentGroup().addToken(t);
	}

	public Token getLastToken() {
		return getCurrentGroup().getLastToken();
	}

	public boolean isSingleGroup() {
		return tokens.size() == 1;
	}
	
	@Override
	public void afterTokenizing(EvaluatorContext evaluator, ExpressionContext expressionContext) {
		tokens.forEach(t -> t.afterTokenizing(evaluator, expressionContext));
	}

	public int size() {
		return tokens.size();
	}

	public List<TokenGroup> getGroups() {
		return tokens;
	}

	public boolean isEmpty() {
		return tokens.isEmpty();
	}


}
