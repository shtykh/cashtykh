package shtykh.storage.cache.internal;

import shtykh.storage.Storage;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class OneLevelCache<Key, Value extends Serializable> implements IOneLevelCache<Key, Value> {

	private final boolean trimToCapacity;
	private int capacity;

	private Storage<Key, Value> storage;
	private LinkedList<Key> keys;

	public OneLevelCache(int capacity, boolean trimToCapacity, Storage<Key, Value> storage) {
		this.capacity = capacity;
		this.trimToCapacity = trimToCapacity;
		this.storage = storage;
		keys = new LinkedList<>();
	}

	// Storage methods

	@Override
	public Value get(Key key) throws IOException {
		Value value = remove(key);
		put(key, value);
		return value;
	}

	@Override
	public Value put(Key key, Value value) throws IOException {
		keys.remove(key);
		keys.offerFirst(key);
		Value put = storage.put(key, value);
		if (trimToCapacity && size() > capacity) {
			pollLast();
		}
		return put;
	}

	@Override
	public Value remove(Key key) throws IOException {
		if (keys.contains(key)) {
			keys.remove(key);
			Value deleted = storage.remove(key);
			return deleted;
		} else {
			return null;
		}
	}

	@Override
	public void clear() throws IOException {
		storage.clear();
	}

	// ICache methods

	@Override
	public int size() {
		return keys.size();
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public boolean containsKey(Key key) {
		return keys.contains(key);
	}

	// IOneLevelCache methods

	@Override
	public Value offerLast(Key key, Value value) throws IOException {
		keys.remove(key);
		keys.offerLast(key);
		return storage.put(key, value);
	}

	@Override
	public Pair<Key, Value> pollLast() throws IOException {
		Key key = keys.pollLast();
		Value value = storage.remove(key);
		return new ImmutablePair<>(key, value);
	}

	@Override
	public Pair<Key, Value> pollFirst() throws IOException {
		Key key = keys.pollFirst();
		Value value = storage.remove(key);
		return new ImmutablePair<>(key, value);
	}

	@Override
	public Iterator<Key> keyIterator() {
		return keys.iterator();
	}

	@Override
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
