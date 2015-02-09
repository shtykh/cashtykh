package shtykh.storage.cache;

import org.apache.commons.lang3.tuple.Pair;
import shtykh.storage.cache.internal.IOneLevelCache;
import shtykh.storage.cache.internal.OneLevelCache;
import shtykh.storage.internal.MemoryStorage;
import shtykh.storage.internal.OsStorage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import static com.google.common.base.Objects.firstNonNull;

/**
 * Created by shtykh on 06/02/15.
 */
public class TwoLevelCache<Key, Value extends Serializable> implements IMultiLevelCache<Key, Value> {

	private IOneLevelCache<Key, Value>[] levels = new OneLevelCache[2];
	private boolean lastOnTop = false;

	public TwoLevelCache(int level0Capacity, int level1Capacity, boolean lastOnTop) {
		levels[0] = new OneLevelCache<>(level0Capacity, false, new MemoryStorage<>());
		levels[1] = new OneLevelCache<>(level1Capacity, true, new OsStorage<>());
		setLastOnTop(lastOnTop);
	}

	// Storage methods

	@Override
	public Value put(Key key, Value value) throws IOException {
		Value toReturn;
		if (levels[1].containsKey(key)) {
			toReturn = levels[1].remove(key);
			levels[0].put(key, value);
		} else {
			toReturn = levels[0].put(key, value);
		}
		pushDownIfNeeded();
		return toReturn;
	}

	@Override
	public Value remove(Key key) throws IOException {
		if (levels[0].containsKey(key)) {
			Value deleted = levels[0].remove(key);
			pushUpIfNeeded();
			return deleted;
		} else {
			return levels[1].remove(key);
		}
	}

	@Override
	public void clear() throws IOException {
		levels[0].clear();
		levels[1].clear();
	}

	// ICache methods

	@Override
	public int size() {
		return levels[0].size() + levels[1].size();
	}

	@Override
	public Value getAndDoNotPutOnTop(Key key) throws IOException {
		return firstNonNull(levels[0].getAndDoNotPutOnTop(key), levels[1].getAndDoNotPutOnTop(key));
	}

	@Override
	public boolean isLastOnTop() {
		return lastOnTop;
	}

	public void setLastOnTop(boolean lastOnTop) {
		this.lastOnTop = lastOnTop;
		levels[0].setLastOnTop(lastOnTop);
		levels[1].setLastOnTop(lastOnTop);
	}

	@Override
	public int getCapacity() {
		return levels[0].getCapacity() + levels[1].getCapacity();
	}

	@Override
	public boolean containsKey(Key key) {
		return levels[0].containsKey(key) || levels[1].containsKey(key);
	}

	// IMultiLevelCache methods

	@Override
	public Iterator<Key> keyIteratorOfLevel(int level) {
		return levels[level].keyIterator();
	}
	@Override
	public int getCapacityOfLevel(int level) {
		return levels[level].getCapacity();
	}

	@Override
	public void setCapacityOfLevel(int level, int capacity) {
		levels[level].setCapacity(capacity);
		try {
			pushUpIfNeeded();
			pushDownIfNeeded();
		} catch (IOException e) {
			throw new RuntimeException("Internal error while changing capacity of cache: \n" + e.getMessage());
		}
	}

	// private methods

	private void pushDownIfNeeded() throws IOException {
		while (levels[0].size() > levels[0].getCapacity()) {
			Pair<Key, Value> down = levels[0].pollLast();
			levels[1].put(down.getLeft(), down.getRight());
		}
		while (levels[1].size() > levels[1].getCapacity()) {
			levels[1].pollLast();
		}
	}

	private void pushUpIfNeeded() throws IOException {
		while (levels[0].size() < levels[0].getCapacity() && levels[1].size() != 0) {
			Pair<Key, Value> up = levels[1].pollFirst();
			levels[0].offerLast(up.getLeft(), up.getRight());
		}
	}
}
