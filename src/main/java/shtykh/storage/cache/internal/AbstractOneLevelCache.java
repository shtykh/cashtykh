package shtykh.storage.cache.internal;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shtykh on 16/02/15.
 */
public abstract class AbstractOneLevelCache<Key, Value extends Serializable>
		extends AbstractCache<Key, Value> 
		implements IOneLevelCache<Key, Value> {
	
	protected abstract Value offerLastSync(Key key, Value value) throws IOException;
	protected abstract Pair<Key, Value> pollLastSync() throws IOException;
	protected abstract Pair<Key, Value> pollFirstSync() throws IOException;

	public Value offerLast(Key key, Value value) throws IOException {
		Value oldValue;
		synchronized (this) {
			oldValue = offerLastSync(key, value);
		}
		return oldValue;
	}

	public Pair<Key, Value> pollLast() throws IOException {
		Pair<Key, Value> valuePair;
		synchronized (this) {
			valuePair = pollLastSync();
		}
		return valuePair;
	}

	public Pair<Key, Value> pollFirst() throws IOException {
		Pair<Key, Value> valuePair;
		synchronized (this) {
			valuePair = pollFirstSync();
		}
		return valuePair;
	}
	
}
