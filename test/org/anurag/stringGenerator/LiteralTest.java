package org.anurag.stringGenerator;

import static org.junit.Assert.*;

import org.anurag.stringGenerator.token.Literal;
import org.junit.Test;

public class LiteralTest {

	@Test
	public void anyPatternCharacter() {
		assertEquals('.', Literal.ANY_PATTERN_LITERAL);
	}

	@Test
	public void branchOperatorCharacter() {
		assertEquals('|', Literal.BRANCH_OPERATOR_LITERAL);
	}

	@Test
	public void backetExpressionsStartCharacter() {
		assertEquals('[', Literal.BRACKET_START_OPERATOR_LITERAL);
	}

	@Test
	public void backetExpressionsEndCharacter() {
		assertEquals(']', Literal.BRACKET_END_OPERATOR_LITERAL);
	}

	@Test
	public void validCharacterTest() {
		try {
			assertTrue(Literal.isCharacterValid(' '));
			assertTrue(Literal.isCharacterValid('a'));
			assertTrue(Literal.isCharacterValid('p'));
			assertTrue(Literal.isCharacterValid('z'));
			assertTrue(Literal.isCharacterValid('A'));
			assertTrue(Literal.isCharacterValid('B'));
			assertTrue(Literal.isCharacterValid('Z'));
			assertTrue(Literal.isCharacterValid('0'));
			assertTrue(Literal.isCharacterValid('1'));
			assertTrue(Literal.isCharacterValid('9'));
			assertTrue(Literal.isCharacterValid('!'));
			assertTrue(Literal.isCharacterValid(','));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void invalidCharacterTest() {
		try {
			assertFalse(Literal.isCharacterValid('\u00A9'));
			assertFalse(Literal.isCharacterValid('\u03B5'));
//			assertFalse(Literal.isCharacterValid('¾'));
//			assertFalse(Literal.isCharacterValid('ú'));
//			assertFalse(Literal.isCharacterValid('有'));
//			assertFalse(Literal.isCharacterValid('म'));
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
