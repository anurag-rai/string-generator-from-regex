package org.anurag.stringGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.anurag.stringGenerator.token.AndEntity;
import org.anurag.stringGenerator.token.DataEntity;
import org.anurag.stringGenerator.token.Literal;
import org.anurag.stringGenerator.token.OrEntity;
import org.junit.Test;

public class BasicGeneratorTest {

	@Test
	public void testParallel() {
		Set<Character> sa = new HashSet<>();
		sa.add('a');
		Set<Character> sb = new HashSet<>();
		sb.add('b');
		Set<Character> sc = new HashSet<>();
		sc.add('c');

		DataEntity da = new DataEntity(sa.stream().map(Literal::getNumericReference).collect(Collectors.toSet()));
		DataEntity db = new DataEntity(sb.stream().map(Literal::getNumericReference).collect(Collectors.toSet()));
		DataEntity dc = new DataEntity(sc.stream().map(Literal::getNumericReference).collect(Collectors.toSet()));

		AndEntity ea = new AndEntity("abc");
		ea.addGenerator(da);
		ea.addGenerator(db);
		ea.addGenerator(dc);

		assertEquals("abc", ea.generate());
	}

	@Test
	public void testSerial() {
		Set<Character> sa = new HashSet<>();
		sa.add('a');
		Set<Character> sb = new HashSet<>();
		sb.add('b');
		Set<Character> sc = new HashSet<>();
		sc.add('c');

		DataEntity da = new DataEntity(Literal.convertCharSetToNumericSet(sa));
		DataEntity db = new DataEntity(Literal.convertCharSetToNumericSet(sb));
		DataEntity dc = new DataEntity(Literal.convertCharSetToNumericSet(sc));

		AndEntity ea = new AndEntity("a");
		ea.addGenerator(da);
		AndEntity eb = new AndEntity("b");
		eb.addGenerator(db);
		AndEntity ec = new AndEntity("c");
		ec.addGenerator(dc);

		OrEntity ep = new OrEntity("a|b|c");
		ep.addGenerator(ea);
		ep.addGenerator(eb);
		ep.addGenerator(ec);

		Set<String> possibleAnswers = new HashSet<>();
		possibleAnswers.add("a");
		possibleAnswers.add("b");
		possibleAnswers.add("c");
		assertTrue(possibleAnswers.contains(ep.generate()));
	}
}
