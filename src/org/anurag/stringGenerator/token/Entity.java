package org.anurag.stringGenerator.token;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity extends Token {
	protected List<StringGenerator> generators;

	protected Repeater r;

	protected String s;

	protected String currentGeneration;

	public Entity(String s) {
		this.generators = new ArrayList<>();
		this.s = s;
		this.currentGeneration = null;
	}

	public void addGenerator(StringGenerator s) {
		generators.add(s);
	}

	public void addGenerators(StringGenerator... listOfSG) {
		for (StringGenerator sg : listOfSG) {
			this.addGenerator(sg);
		}
	}

	public void setRepeater(Repeater r) {
		this.r = r;
	}

	public String toString() {
		return this.s;
	}
}
