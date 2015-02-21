package shtykh.storage.cache.internal;

import org.apache.commons.lang3.tuple.Pair;
import shtykh.storage.cache.ICache;
import shtykh.tweets.tag.IKey;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shtykh on 09/02/15.
 */
public interface IOneLevelCache<Key, Value extends Serializable> extends ICache<Key, Value> {
	public void setCapacity(int capacity);

	public Value offerLast(Key key, Value value) throws IOException;

	public Pair<Key, Value> pollLast() throws IOException;

	public Pair<Key, Value> pollFirst() throws IOException;
}
