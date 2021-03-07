package org.anurag.stringGenerator;

import java.util.*;

import org.anurag.stringGenerator.token.AndEntity;
import org.anurag.stringGenerator.token.BackRefEntity;
import org.anurag.stringGenerator.token.BranchOperator;
import org.anurag.stringGenerator.token.DataEntity;
import org.anurag.stringGenerator.token.Entity;
import org.anurag.stringGenerator.token.Literal;
import org.anurag.stringGenerator.token.OrEntity;
import org.anurag.stringGenerator.token.Repeater;
import org.anurag.stringGenerator.token.StringGenerator;
import org.anurag.stringGenerator.token.Token;

public class RegExpBuilder {

	private Map<Integer, Entity> map;
	private int currentGroup = 0;

	public RegExpBuilder() {
		map = new HashMap<>();
	}

	public StringGenerator build(String s) throws Exception {
		return this.build(s, 0, s.length());
	}

	private StringGenerator build(String s, int start, int end) throws Exception {

		// STEP 1: Reduce
		Stack<Token> tokens = new Stack<>();
		int currentCharacterPos = start;
		while (currentCharacterPos < end) {
			Token token = null;
			char c = s.charAt(currentCharacterPos);
			switch (c) {
			case Literal.BRANCH_OPERATOR_LITERAL: {
				if (tokens.isEmpty() || (!tokens.isEmpty() && tokens.peek() instanceof BranchOperator))
					throw new Error("Incorrect branch operators");

				concatTillLastBranch(tokens);

				currentCharacterPos++;
				token = new BranchOperator();

				break;
			}
			case Literal.ESCAPE_OPERATOR_LITERAL: {
				if (currentCharacterPos == (end - 1))
					throw new Exception("Invalid escape at character position " + currentCharacterPos);

				// Handle backreference (only single-digit)
				char nextCharacter = s.charAt(currentCharacterPos + 1);

				StringGenerator e;
				if (Literal.isDigit(nextCharacter) && map.keySet().contains(Literal.getLiteralAsNumber(nextCharacter))) {
					String backRefString = "" + c + nextCharacter;
					e = buildBackRefEntity(backRefString, Literal.getLiteralAsNumber(nextCharacter));
				} else {
					e = buildCharacterEntity(nextCharacter);
				}

				token = (Token) e;
				currentCharacterPos = currentCharacterPos + 2;
				break;
			}
			case Literal.BRACKET_START_OPERATOR_LITERAL: {
				int secondPointer = currentCharacterPos + 1;
				StringBuilder insideSquareBracket = new StringBuilder();
				while (secondPointer < end && s.charAt(secondPointer) != Literal.BRACKET_END_OPERATOR_LITERAL) {
					insideSquareBracket.append(s.charAt(secondPointer));
					secondPointer++;
				}
				if (secondPointer >= end)
					throw new Exception("Could not find matching bracket-end for an opening bracket-start at " + currentCharacterPos);
				if (insideSquareBracket.length() == 0)
					throw new Exception("Square bracket expression cannot be empty");
				StringGenerator e = buildSquareBracketEntity(insideSquareBracket.toString());
				token = (Token) e;
				currentCharacterPos = secondPointer + 1;
				break;
			}
			case Literal.GROUP_START_LITERAL: {
				int secondPointer = currentCharacterPos + 1;
				int open = 1;
				while (secondPointer < end) {
					if (s.charAt(secondPointer) == Literal.GROUP_END_LITERAL) {
						open--;
					} else if (s.charAt(secondPointer) == Literal.GROUP_START_LITERAL) {
						open++;
					}
					if (open == 0)
						break;
					secondPointer++;
				}
				if (open > 0)
					throw new Exception("Could not find ending group for opening at " + currentCharacterPos);
				if ((secondPointer - currentCharacterPos) < 2)
					throw new Exception("Not a valid group expression");

				this.currentGroup++;
				int groupId = this.currentGroup;

				StringGenerator e = this.build(s, currentCharacterPos + 1, secondPointer);
				// TODO: regExp.setEnclosingParans();

				map.put(groupId, (Entity) e);

				token = (Token) e;
				currentCharacterPos = secondPointer + 1;
				break;
			}
			case Literal.ANY_PATTERN_LITERAL: {
				StringGenerator e = buildAnyPatternEntity();
				token = (Token) e;
				currentCharacterPos++;
				break;
			}
			default: {
				StringGenerator e = buildCharacterEntity(s.charAt(currentCharacterPos));
				token = (Token) e;
				currentCharacterPos++;
				break;
			}
			}
			if (!(token instanceof BranchOperator) && currentCharacterPos < end && RepeaterBuilder.looksLikeRepeater(s, currentCharacterPos)) {
				Repeater r = RepeaterBuilder.build(s, currentCharacterPos, end);
				Entity e = (Entity) token;
				e.setRepeater(r);
				currentCharacterPos = currentCharacterPos + r.toString().length();
			}

			tokens.push(token);
		}

		concatTillLastBranch(tokens);

		return mergeBranches(tokens);

	}

	private StringGenerator buildCharacterEntity(char character) throws Exception {
		Set<Character> allowedCharacters = Literal.getValidSetFromCharacters(character);

		if (allowedCharacters.isEmpty()) {
			throw new Exception("Failed to build character entity");
		}
		DataEntity de = new DataEntity(Literal.convertCharSetToNumericSet(allowedCharacters));
		AndEntity e = new AndEntity("" + character);
		e.addGenerator(de);
		return e;

	}

	private StringGenerator buildSquareBracketEntity(String s) throws Exception {
		Set<Character> allowedCharacters;
		if (s.length() == 0)
			throw new Exception("Failed to build sqaure bracket entity for empty string");
		if (s.charAt(0) == '^') {
			allowedCharacters = Literal.getValidSetFromNegativeList(s.substring(1));
		} else {
			allowedCharacters = Literal.getValidSetFromPositiveList(s);
		}

		if (allowedCharacters.isEmpty()) {
			throw new Exception("Failed to build character entity");
		}
		DataEntity de = new DataEntity(Literal.convertCharSetToNumericSet(allowedCharacters));
		AndEntity e = new AndEntity(Literal.BRACKET_START_OPERATOR_LITERAL + s + Literal.BRACKET_END_OPERATOR_LITERAL);
		e.addGenerator(de);
		return e;

	}

	private StringGenerator buildAnyPatternEntity() throws Exception {
		Set<Character> allowedCharacters = Literal.getAllowedSet();

		if (allowedCharacters.isEmpty()) {
			throw new Exception("Failed to build any pattern regex");
		}
		DataEntity de = new DataEntity(Literal.convertCharSetToNumericSet(allowedCharacters));
		AndEntity e = new AndEntity(Literal.getLiteralAsString(Literal.ANY_PATTERN_LITERAL));
		e.addGenerator(de);
		return e;
	}

	private StringGenerator buildBackRefEntity(String s, int id) {
		Entity backReferecedEntity = map.get(id);
		BackRefEntity bRefE = new BackRefEntity(s, backReferecedEntity);
		return bRefE;

	}

	private void concatTillLastBranch(Stack<Token> stack) {
		Stack<Token> tempStack = new Stack<>();
		while (!stack.isEmpty() && !(stack.peek() instanceof BranchOperator)) {
			tempStack.push(stack.pop());
		}
		if (tempStack.isEmpty())
			return;

		String name = getNameFromStack(tempStack, "");

		AndEntity start = new AndEntity(name);
		while (!tempStack.isEmpty()) {
			start.addGenerator(tempStack.pop());
		}
		stack.push(start);
	}

	private StringGenerator mergeBranches(Stack<Token> stack) {
		Stack<Token> tempStack = new Stack<>();
		while (!stack.isEmpty()) {
			Token t = stack.pop();
			if (!(t instanceof BranchOperator))
				tempStack.push(t);
		}

		String name = getNameFromStack(tempStack, Literal.getLiteralAsString(Literal.BRANCH_OPERATOR_LITERAL));

		OrEntity branchEntity = new OrEntity(name);
		while (!tempStack.isEmpty()) {
			branchEntity.addGenerator(tempStack.pop());
		}
		return branchEntity;
	}

	private String getNameFromStack(Stack<Token> s, String operator) {
		Stack<Token> temp = new Stack<>();
		String toReturn = "";
		while (!s.isEmpty()) {
			Token t = s.pop();
			toReturn += t.toString() + operator;
			temp.push(t);
		}
		while (!temp.isEmpty()) {
			s.push(temp.pop());
		}
		return toReturn.substring(0, toReturn.length() - operator.length());
	}
}
