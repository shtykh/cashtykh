package cashtykh;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class OneLevelCache<Value> extends LinkedHashMap<String, Value> implements ICache<String, Value> {

	private final int capacity;

	public OneLevelCache(int capacity) {
		super(16, 0.75f, true);
		this.capacity = capacity;
	}

	@Override
	public Value retrieve(String key) throws NoSuchElementException {
		return super.get(key);
	}

	@Override
	public void cache(String key, Value value) {
		super.put(key, value);
	}

	@Override
	public boolean delete(String key) {
		return null != super.remove(key);
	}

	@Override
	public Iterator<String> firstLevelIterator() {
		return super.keySet().iterator();
	}

	@Override
	public Iterator<String> secondLevelIterator() {
		throw new NotImplementedException();
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<String, Value> eldest) {
		return size() >= capacity;
	}
}
