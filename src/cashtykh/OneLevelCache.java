package cashtykh;

import com.sun.tools.javac.util.Pair;

import java.util.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class OneLevelCache<Key, Value> implements ICache<Key, Value> {

	private int capacity;
	private final boolean controlCapacity;

	private Storage <Key, Value> cache;

	private LinkedList<Key> keys;

	public OneLevelCache(int capacity, boolean controlCapacity, Storage<Key, Value> cache) {
		this.capacity = capacity;
		this.controlCapacity = controlCapacity;
		this.cache = cache;
		keys = new LinkedList<>();
	}

	@Override
	public Value get(Key key) throws NoSuchElementException {
		Value value = remove(key);
		put(key, value);
		return value;
	}

	@Override
	public Value put(Key key, Value value) {
		keys.remove(key);
		keys.offerFirst(key);
		Value put = cache.put(key, value);
		if (controlCapacity && size() > capacity) {
			pollLast();
		}
		return put;
	}

	@Override
	public boolean containsKey(Key key) {
		return cache.containsKey(key);
	}

	@Override
	public Value remove(Key key) {
		if (keys.contains(key)) {
			keys.remove(key);
			Value deleted = cache.remove(key);
			return deleted;
		} else {
			return null;
		}
	}

	public Iterator<Key> iterator() {
		return keys.iterator();
	}

	@Override
	public int size() {
		return keys.size();
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public Value offerLast(Key key, Value value) {
		keys.remove(key);
		keys.offerLast(key);
		return cache.put(key, value);
	}

	@Override
	public Pair<Key, Value> pollLast() {
		Key key = keys.pollLast();
		Value value = cache.remove(key);
		return new Pair<>(key, value);
	}

	@Override
	public Pair<Key, Value> pollFirst() {
		Key key = keys.pollFirst();
		Value value = cache.remove(key);
		return new Pair<>(key, value);
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
