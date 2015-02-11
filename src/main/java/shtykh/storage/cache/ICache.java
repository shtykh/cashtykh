package shtykh.storage.cache;

import shtykh.storage.Storage;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shtykh on 06/02/15.
 */
public interface ICache<Key, Value extends Serializable> extends Storage<Key, Value>, Iterable<Key> {
	public default Value get(Key key) throws IOException {
		if (isLastOnTop()) {
			Value value = remove(key);
			put(key, value);
			return value;
		} else {
			return getAndDoNotPutOnTop(key);
		}
	}

	Value getAndDoNotPutOnTop(Key key) throws IOException;

	public boolean isLastOnTop();
	public void setLastOnTop(boolean lastOnTop);
	public int getCapacity();
	public boolean containsKey(Key key);
	public int size();
}
