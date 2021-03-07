package org.anurag.stringGenerator.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.anurag.stringGenerator.util.Util;

public class DataEntity implements StringGenerator {

	private List<Integer> allowed;
	private int sampleSpace;

	public DataEntity(Set<Integer> allowed) {
		this.allowed = new ArrayList<>(allowed);
		this.sampleSpace = allowed.size();
	}

	@Override
	public String generate() {
		int randomIndex = Util.getRandomInRange(0, this.sampleSpace);
		return "" + Literal.getCharacter(this.allowed.get(randomIndex));
	}
}
