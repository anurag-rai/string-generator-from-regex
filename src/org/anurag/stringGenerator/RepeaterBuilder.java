package org.anurag.stringGenerator;

import java.util.HashSet;
import java.util.Set;

import org.anurag.stringGenerator.token.Repeater;

public class RepeaterBuilder {
	public static boolean looksLikeRepeater(String s, int index) {
		Set<Character> allowedQuantifierStarts = new HashSet<>();
		allowedQuantifierStarts.add('+');
		allowedQuantifierStarts.add('*');
		allowedQuantifierStarts.add('?');
		allowedQuantifierStarts.add('{');
		return allowedQuantifierStarts.contains(s.charAt(index));
	}

	public static Repeater build(String s, int index, int endIndex) throws Exception {
		String errorMessage = "Cannot parse quantifier at index " + index + " for " + s;
		char c = s.charAt(index);
		endIndex = Math.min(endIndex, s.length());
		Repeater r;
		if (c == '+') {
			r = new Repeater("+");
			r.setMin(1);
		} else if (c == '*') {
			r = new Repeater("*");
			r.setMin(0);
		} else if (c == '?') {
			r = new Repeater("?");
			r.setMin(0);
			r.setMax(1);
		} else if (c == '{') {
			int secondPointer = index + 1;
			StringBuilder first = new StringBuilder();
			StringBuilder second = new StringBuilder();
			int comma = 0;
			while (secondPointer < endIndex && s.charAt(secondPointer) != '}') {
				char charInside = s.charAt(secondPointer);
				if (charInside == ',') {
					comma++;
					if (comma > 1)
						throw new Exception(errorMessage);
				} else if (comma == 0) {
					first.append(charInside);
				} else {
					second.append(charInside);
				}
				secondPointer++;
			}
			if (secondPointer >= endIndex || (first.length() == 0 && second.length() == 0))
				throw new Exception(errorMessage);

			try {
				int min = first.length() > 0 ? Integer.parseInt(first.toString()) : 0;
				int max = (comma == 0) ? min : (second.length() > 0 ? Integer.parseInt(second.toString()) : Repeater.MAX);
				if (max - min < 0) {
					throw new Exception("Invalid range");
				}
				r = new Repeater("{" + first.toString() + (comma == 0 ? "" : ("," + second.toString())) + "}");
				r.setMin(min);
				if (max > 0)
					r.setMax(max);
			} catch (Exception e) {
				throw new Exception(errorMessage + ": " + e.getMessage());
			}

		} else {
			throw new Exception(errorMessage);
		}
		return r;
	}
}
