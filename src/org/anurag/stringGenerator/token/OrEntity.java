package org.anurag.stringGenerator.token;

import java.util.concurrent.ThreadLocalRandom;

import org.anurag.stringGenerator.util.Util;

public class OrEntity extends Entity {

	public OrEntity(String s) {
		super(s);
	}

	@Override
	public String generate() {
		int iterations = 1;
		if (r != null) {
			iterations = Util.getRandomInRange(r.getMin(), r.getMax() + 1);
		}
		String toReturn = "";
		while (iterations-- > 0) {

			int x = ThreadLocalRandom.current().nextInt(0, generators.size());
			toReturn += generators.get(x).generate();

		}

		currentGeneration = toReturn;

		return toReturn;
	}

}
