package org.anurag.stringGenerator;

import static org.junit.Assert.*;

import org.anurag.stringGenerator.token.Repeater;
import org.junit.Test;

public class RepeaterTest {

	@Test
	public void noComma() {
		try {
			Repeater r = getRepeater("{6}", 0, 3);
			assertEquals(6, r.getMin());
			assertEquals(6, r.getMax());
			assertEquals(3, r.toString().length());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void plusWithIndeK() {
		try {
			Repeater r = getRepeater("z+", 1, 2);
			assertEquals(1, r.getMin());
			assertEquals(Repeater.MAX, r.getMax());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void questionWithIndeK() {
		try {
			Repeater r = getRepeater("zz?", 2, 3);
			assertEquals(0, r.getMin());
			assertEquals(1, r.getMax(), 1);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void starWithIndeK() {
		try {
			Repeater r = getRepeater("zzz*", 3, 4);
			assertEquals(r.getMin(), 0);
			assertEquals(r.getMax(), 1);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void minMax() {
		try {
			Repeater r = getRepeater("{3,5}", 0, 5);
			assertEquals(3, r.getMin());
			assertEquals(5, r.getMax());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void minMaxWithIndeK() {
		try {
			Repeater r = getRepeater("abc{3,6}", 3, 8);
			assertEquals(3, r.getMin());
			assertEquals(6, r.getMax());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void minMaxMissingMaxWithIndeK() {
		try {
			Repeater r = getRepeater("zzzz{9,}", 4, 8);
			assertEquals(9, r.getMin());
			assertEquals(Repeater.MAX, r.getMax());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void minMaxMissingMin() {
		try {
			Repeater r = getRepeater("{,7}", 0, 4);
			assertEquals(Repeater.MIN, r.getMin());
			assertEquals(7, r.getMax());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void minMaxAbsurdRange() {
		try {
			Repeater r = getRepeater("{3,5}", 0, 1000);
			assertEquals(r.getMin(), 3);
			assertEquals(r.getMax(), 5);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void minMax0PrefixNumbers() {
		try {
			Repeater r = getRepeater("{01,002}", 0, 8);
			assertEquals(1, r.getMin());
			assertEquals(2, r.getMax());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void minMaxPrematureEndIndex() {
		try {
			getRepeater("abc{3,6}", 3, 4);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void minMaxEndIndexLessThanStart() {
		try {
			getRepeater("zzzz{9,}", 4, 3);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void minMaxPermatureStartIndex() {
		try {
			getRepeater("z{1,2}", 0, 5);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void minMaxEmpty() {
		try {
			getRepeater("{}", 0, 1);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void minMaxWrongIndex() {
		try {
			getRepeater("z?", 0, 1);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void minMaxTooManyComma() {
		try {
			getRepeater("{1,2,3}", 0, 6);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void minMaxTooManyCommaMissingNumber() {
		try {
			getRepeater("{1,2,}", 0, 5);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void minMaxTooManyCommaMissingNumber2() {
		try {
			getRepeater("{,1,}", 0, 4);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	@Test
	public void minMaxTooManyCommaMissingNumber3() {
		try {
			getRepeater("{,1,2", 0, 4);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	public static Repeater getRepeater(String s, int index, int end) throws Exception {
		return RepeaterBuilder.build(s, index, end);
	}

}
