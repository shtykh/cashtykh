package cashtykh;

import util.Serializer;

import java.util.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class OneLevelCache<Key, Value> implements ICache<Key, Value> {

	private final int capacity;

	private ICache <Key, Value> cache;

	private LinkedList<Key> keys;

	public OneLevelCache(int capacity, ICache<Key, Value> cache) {
		this.capacity = capacity;
		this.cache = cache;
		keys = new LinkedList<>();
	}

	@Override
	public Value retrieve(Key key) throws NoSuchElementException {
		Value value = delete(key);
		cache(key, value);
		return value;
	}

	@Override
	public void cache(Key key, Value value) {
		keys.offerFirst(key);
		cache.cache(key, value);
	}

	@Override
	public Value delete(Key key) {
		if (keys.contains(key)) {
			keys.remove(key);
			Value deleted = cache.delete(key);
			return deleted;
		} else {
			return null;
		}
	}

	@Override
	public Iterator<Key> iterator() {
		return keys.iterator();
	}

	@Override
	public int size() {
		return keys.size();
	}
}
