package shtykh.storage.cache;

import shtykh.storage.Storage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by shtykh on 06/02/15.
 */
public interface ICache<Key, Value extends Serializable> extends Storage<Key, Value>, Iterable<Key> {
	@Override
	public default Value get(Key key) throws IOException {
		if (isLastOnTop()) {
			Value value;
			synchronized (this) {
				value = remove(key);
				put(key, value);
			}
			return value;
		} else {
			return getAndDoNotPutOnTop(key);
		}
	}
	
	@Override
	public Value put(Key key, Value value) throws IOException;

	@Override
	public Value remove(Key key) throws IOException;

	public boolean isLastOnTop();

	public int getCapacity();
	public boolean containsKey(Key key);
	public Collection<Key> keys();
	public int size();
	public void setLastOnTop(boolean lastOnTop);

	Value getAndDoNotPutOnTop(Key key) throws IOException;
}
