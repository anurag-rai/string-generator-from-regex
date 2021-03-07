package org.anurag.stringGenerator.token;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class Literal {
	public static final char ANY_PATTERN_LITERAL = '.';
	public static final char BRANCH_OPERATOR_LITERAL = '|';
	public static final char ESCAPE_OPERATOR_LITERAL = '\\';
	public static final char BRACKET_START_OPERATOR_LITERAL = '[';
	public static final char BRACKET_END_OPERATOR_LITERAL = ']';
	public static final char GROUP_START_LITERAL = '(';
	public static final char GROUP_END_LITERAL = ')';

	private static Set<Integer> allowedRange;

	static {
		allowedRange = new HashSet<>();
		// ASCII
		for (int i = 32; i < 127; i++) {
			allowedRange.add(i);
		}
	}

	private Literal() {

	}

	public static String getLiteralAsString(char c) {
		return "" + c;
	}

	public static int getLiteralAsNumber(char c) {
		return Integer.parseInt("" + c);
	}

	public static boolean isDigit(char c) {
		int numericValue = (int) c;
		return numericValue >= 48 && numericValue <= 57;
	}

	public static boolean isCharacterValid(char c) {
		return allowedRange.contains((int) c);
	}

	public static boolean isCharacterValid(int c) {
		return allowedRange.contains(c);
	}

	public static char getCharacter(int x) {
		return (char) x;
	}

	public static int getNumericReference(char c) {
		return (int) c;
	}

	public static Set<Integer> convertCharSetToNumericSet(Set<Character> s) {
		return s.stream().map(Literal::getNumericReference).collect(Collectors.toSet());
	}

	public static Set<Character> getNegationList(Set<Character> s) {
		Set<Character> negationSet = new HashSet<>();
		for (int x : allowedRange) {
			char c = getCharacter(x);
			if (!s.contains(c)) {
				negationSet.add(c);
			}
		}
		return negationSet;
	}

	public static Set<Character> getAllowedSet() {
		Set<Character> set = new HashSet<>();
		for (int x : allowedRange) {
			if (isCharacterValid(x)) {
				set.add(getCharacter(x));
			}
		}
		return set;
	}

	public static Set<Character> getValidSetFromPositiveList(String s) throws Exception {
		int N = s.length();
		int index = 0;

		Set<Character> validSet = new HashSet<>();
		while (index < N) {
			if ((index + 2) < N && s.charAt(index + 1) == '-') {
				// is this a valid range
				int startingRange = Literal.getNumericReference(s.charAt(index));
				int endingRange = Literal.getNumericReference(s.charAt(index + 2));
				if (startingRange > endingRange)
					throw new Exception("Invalid range");
				Set<Character> allowedCharacters = getAllowedCharactersInRange(startingRange, endingRange);

				validSet.addAll(allowedCharacters);

				index = index + 3;
			} else {
				char c = s.charAt(index);
				if (Literal.isCharacterValid(c)) {
					validSet.add(c);
					index++;
				} else {
					throw new Exception("Invalid character present");
				}
			}
		}

		return validSet;
	}

	public static Set<Character> getValidSetFromNegativeList(String s) throws Exception {
		int N = s.length();
		int index = 0;

		Set<Character> validSet = Literal.getAllowedSet();
		while (index < N) {
			if ((index + 2) < N && s.charAt(index + 1) == '-') {
				// is this a valid range
				int startingRange = Literal.getNumericReference(s.charAt(index));
				int endingRange = Literal.getNumericReference(s.charAt(index + 2));
				if (startingRange > endingRange)
					throw new Exception("Invalid range");
				Set<Character> allowedCharacters = getAllowedCharactersInRange(startingRange, endingRange);

				// remove allowedCharacters from validSet
				validSet.removeAll(allowedCharacters);
				index = index + 3;
			} else {
				char c = s.charAt(index);
				if (Literal.isCharacterValid(c)) {
					validSet.add(c);
					index++;
				} else {
					throw new Exception("Invalid character present");
				}
			}
		}

		return validSet;
	}

	public static Set<Character> getAllowedCharactersInRange(int start, int end) {
		Set<Character> allowed = new HashSet<>();
		for (int i = start; i <= end; i++) {
			if (Literal.isCharacterValid(i)) {
				allowed.add(Literal.getCharacter(i));
			}
		}
		return allowed;
	}

	public static Set<Character> getValidSetFromCharacters(char... chars) {
		Set<Character> allowed = new HashSet<>();
		for (char c : chars) {
			if (Literal.isCharacterValid(c)) {
				allowed.add(c);
			}
		}
		return allowed;
	}

}
