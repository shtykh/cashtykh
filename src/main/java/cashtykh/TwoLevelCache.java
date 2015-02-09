package cashtykh;

import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class TwoLevelCache<Key, Value extends Serializable> implements ICache<Key, Value> {

	private OneLevelCache<Key, Value> level1;
	private OneLevelCache<Key, Value> level2;


	public TwoLevelCache(int firstLevelCapacity, int secondLevelCapacity) {
		level1 = new OneLevelCache<>(firstLevelCapacity, false, new MemoryStorage<>());
		level2 = new OneLevelCache<>(secondLevelCapacity, true, new OsStorage<>());
	}

	@Override
	public Value get(Key key) throws NoSuchElementException {
		Value value = remove(key);
		put(key, value);
		return value;
	}

	@Override
	public Value put(Key key, Value value) {
		Value toReturn;
		if (level2.containsKey(key)) {
			toReturn = level2.remove(key);
			level1.put(key, value);
		} else {
			toReturn = level1.put(key, value);
		}
		pushDownIfNeeded();
		return toReturn;
	}

	@Override
	public boolean containsKey(Key key) {
		return level1.containsKey(key) || level2.containsKey(key);
	}

	@Override
	public Value remove(Key key) {
		if (level1.containsKey(key)) {
			Value deleted = level1.remove(key);
			pushUpIfNeeded();
			return deleted;
		} else {
			return level2.remove(key);
		}
	}

	private void pushDownIfNeeded() {
		while (level1.size() > level1.getCapacity()) {
			Pair<Key, Value> down = level1.pollLast();
			level2.put(down.getLeft(), down.getRight());
		}
	}

	private void pushUpIfNeeded() {
		while (level1.size() < level1.getCapacity() && level2.size() != 0) {
			Pair<Key, Value> up = level2.pollFirst();
			level1.offerLast(up.getLeft(), up.getRight());
		}
	}


	public Iterator<Key> firstLevelIterator() {
		return level1.iterator();
	}

	public Iterator<Key> secondLevelIterator() {
		return level2.iterator();
	}

	@Override
	public int size() {
		return level1.size() + level2.size();
	}

	@Override
	public int getCapacity() {
		return level1.getCapacity() + level2.getCapacity();
	}

	@Override
	public Value offerLast(Key key, Value value) {
		return level2.offerLast(key, value);
	}

	@Override
	public Pair<Key, Value> pollLast() {
		return level2.pollLast();
	}

	@Override
	public Pair<Key, Value> pollFirst() {
		return level1.pollFirst();
	}

	public int getCapacity1() {
		return level1.getCapacity();
	}

	public int getCapacity2() {
		return level2.getCapacity();
	}

	public void setCapacity1(int capacity1) {
		level1.setCapacity(capacity1);
		pushDownIfNeeded();
		pushUpIfNeeded();
	}

	public void setCapacity2(int capacity2) {
		level2.setCapacity(capacity2);
		pushDownIfNeeded();
		pushUpIfNeeded();
	}
}
