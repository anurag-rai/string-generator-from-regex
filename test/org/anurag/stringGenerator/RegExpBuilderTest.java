package org.anurag.stringGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.anurag.stringGenerator.token.StringGenerator;
import org.junit.Before;
import org.junit.Test;

public class RegExpBuilderTest {

	private static final int numOfGenerations = 10;

	private RegExpBuilder regExpBuilder;

	@Before
	public void init() {
		regExpBuilder = new RegExpBuilder();
	}

	@Test
	public void andOperation() {
		String s = "abc";
		String expected = "abc";
		try {
			StringGenerator generator = regExpBuilder.build(s);
			for (int iter = 0; iter < numOfGenerations; iter++) {
				String actual = generator.generate();
				assertEquals(expected, actual);
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void grouping() {
		String s = "((abc){2}((def){3}(ghi){1})(jkl){1}){2}((xy)(z){2})";
		String expected = "abcabcdefdefdefghijklabcabcdefdefdefghijklxyzz";
		try {
			StringGenerator generator = regExpBuilder.build(s);
			for (int iter = 0; iter < numOfGenerations; iter++) {
				String actual = generator.generate();
				assertEquals(expected, actual);
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void backReference() {
		String s = "(['\"])hello\\1";
		Set<String> expected = new HashSet<>();
		expected.add("\"hello\"");
		expected.add("'hello'");
		try {
			StringGenerator generator = regExpBuilder.build(s);
			for (int iter = 0; iter < numOfGenerations; iter++) {
				String actual = generator.generate();
				assertTrue(expected.contains(actual));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
