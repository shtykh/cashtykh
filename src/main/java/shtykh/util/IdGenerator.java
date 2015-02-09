package shtykh.util;

/**
 * Created by shtykh on 06/02/15.
 */
public class IdGenerator {
	private int id = 0;
	private final String prefix;

	public IdGenerator(String prefix) {
		this.prefix = prefix;
	}

	public int nextId() {
		return id ++;
	}

	public String nextName() {
		return prefix + "_" + nextId();
	}
}
