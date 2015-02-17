package shtykh.storage.cache.internal;

import shtykh.storage.keys.SortedKeySet;
import shtykh.storage.Storage;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import shtykh.storage.keys.Histogram;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class OneLevelCache<Key, Value extends Serializable> extends AbstractOneLevelCache<Key, Value>{

	private final boolean trimToCapacity;
	private int capacity;

	private Storage<Key, Value> storage;
	private SortedKeySet<Key> keys;

	private boolean lastOnTop = false;

	public OneLevelCache(int capacity, boolean trimToCapacity, Storage<Key, Value> storage) {
		this.capacity = capacity;
		this.trimToCapacity = trimToCapacity;
		this.storage = storage;
		keys = new Histogram<>();
	}

	// AbstractCache methods

	@Override
	protected Value putSync(Key key, Value value) throws IOException {
		keys.offerFirst(key);
		Value put = storage.put(key, value);
		if (trimToCapacity && size() > capacity) {
			pollLast();
		}
		return put;
	}

	@Override
	protected Value removeSync(Key key) throws IOException {
		if (keys.contains(key)) {
			keys.remove(key);
			return storage.remove(key);
		} else {
			return null;
		}
	}

	@Override
	public void clearSync() throws IOException {
		keys().clear();
		storage.clear();
	}

	// ICache methods

	@Override
	public int size() {
		return keys.size();
	}

	@Override
	public Value getAndDoNotPutOnTop(Key key) throws IOException {
		return storage.get(key);
	}

	@Override
	public boolean isLastOnTop() {
		return lastOnTop;
	}

	public void setLastOnTop(boolean lastOnTop) {
		this.lastOnTop = lastOnTop;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public boolean containsKey(Key key) {
		return keys.contains(key);
	}

	@Override
	public Collection<Key> keys() {
		return keys.toSortedList();
	}

	// IOneLevelCache methods

	@Override
	public Value offerLastSync(Key key, Value value) throws IOException {
		keys.offerLast(key);
		return storage.put(key, value);
	}

	@Override
	public Pair<Key, Value> pollLastSync() throws IOException {
		Key key = keys.pollLast();
		Value value = storage.remove(key);
		return new ImmutablePair<>(key, value);
	}

	@Override
	public Pair<Key, Value> pollFirstSync() throws IOException {
		Key key = keys.pollFirst();
		Value value = storage.remove(key);
		return new ImmutablePair<>(key, value);
	}

	@Override
	public Iterator<Key> iterator() {
		return keys.iterator();
	}

	@Override
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
