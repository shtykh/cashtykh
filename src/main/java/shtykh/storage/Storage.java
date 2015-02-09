package shtykh.storage;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shtykh on 07/02/15.
 */
public interface Storage<Key, Value extends Serializable> {

	default public Value get(Key key) throws IOException {
		Value value = remove(key);
		put(key, value);
		return value;

	}

	public Value put(Key key, Value value) throws IOException;
	public Value remove(Key key) throws IOException;
	public void clear() throws IOException;
}
