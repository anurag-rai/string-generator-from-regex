package org.anurag.stringGenerator.token;

import java.util.concurrent.ThreadLocalRandom;

public class Repeater {

	public static final int MIN = 0;
	public static final int MAX = 77;

	private int min;
	private int max;
	private String s;

	public Repeater(String s) {
		this(s, MIN, MAX);
	}

	public Repeater(String s, int min, int max) {
		this.s = s;
		this.min = Math.max(min, MIN);
		this.max = Math.min(max, MAX);
	}

	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}

	public void setMin(int min) {
		this.min = Math.max(min, MIN);
	}

	public void setMax(int max) {
		this.max = Math.min(max, MAX);
	}

	public int getRandomInRange() {
		return ThreadLocalRandom.current().nextInt(this.min, this.max + 1);
	}

	@Override
	public String toString() {
		return this.s;
	}
}
