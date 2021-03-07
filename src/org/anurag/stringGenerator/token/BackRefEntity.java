package org.anurag.stringGenerator.token;


public class BackRefEntity extends Entity {

	private Entity parent;

	public BackRefEntity(String s, Entity parent) {
		super(s);
		this.parent = parent;
	}

	@Override
	public String generate() {
		return this.parent.currentGeneration;
	}
}
