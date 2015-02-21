package shtykh.util.init;

/**
 * Created by shtykh on 17/02/15.
 */
public abstract class Factory<Value> {
	public abstract Value make(String name, String line);
}
