package org.anurag.stringGenerator.token;

import org.anurag.stringGenerator.util.Util;

public class AndEntity extends Entity {

	public AndEntity(String s) {
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
			for (StringGenerator g : this.generators) {
				toReturn += g.generate();
			}
		}

		currentGeneration = toReturn;

		return toReturn;
	}

}
