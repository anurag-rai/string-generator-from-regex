package org.anurag.stringGenerator.util;

import java.util.concurrent.ThreadLocalRandom;

public final class Util {

	private Util() {

	}

	// min inclsuive
	// max not inclusive
	public static int getRandomInRange(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

}
