package shtykh.storage.cache.internal;

import shtykh.storage.cache.ICache;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shtykh on 16/02/15.
 */
public abstract class AbstractCache<Key, Value extends Serializable> implements ICache<Key, Value> {

	@Override
	public final Value put(Key key, Value value) throws IOException {
		Value oldValue;
		synchronized (this) {
			oldValue = putSync(key, value);
		}
		return oldValue;
	}

	@Override
	public final Value remove(Key key) throws IOException {
		Value oldValue;
		synchronized (this) {
			oldValue = removeSync(key);
		}
		return oldValue;
	}

	@Override
	public void clear() throws IOException {
		synchronized(this) {
			clearSync();
		}
	}

	protected abstract void clearSync() throws IOException;
	protected abstract Value removeSync(Key key) throws IOException;
	protected abstract Value putSync(Key key, Value value) throws IOException;
}
