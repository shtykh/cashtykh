package shtykh.storage.cache;

import org.apache.commons.lang3.tuple.Pair;
import shtykh.storage.cache.internal.AbstractCache;
import shtykh.storage.cache.internal.IOneLevelCache;
import shtykh.storage.cache.internal.OneLevelCache;
import shtykh.storage.internal.MemoryStorage;
import shtykh.storage.internal.OsStorage;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Objects.firstNonNull;

/**
 * Created by shtykh on 06/02/15.
 */
public class TwoLevelCache<Key, Value extends Serializable> 
		extends AbstractCache<Key, Value> 
		implements IMultiLevelCache<Key, Value> {

	private IOneLevelCache<Key, Value>[] levels = new OneLevelCache[2];
	private boolean lastOnTop = false;

	public TwoLevelCache(int level0Capacity, int level1Capacity, boolean lastOnTop) {
		levels[0] = new OneLevelCache<>(level0Capacity, false, new MemoryStorage<>());
		levels[1] = new OneLevelCache<>(level1Capacity, true, new OsStorage<>());
		setLastOnTop(lastOnTop);
	}

	// AbstractCache methods

	@Override
	protected Value putSync(Key key, Value value) throws IOException {
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
	protected Value removeSync(Key key) throws IOException {
		if (levels[0].containsKey(key)) {
			Value deleted = levels[0].remove(key);
			pushUpIfNeeded();
			return deleted;
		} else {
			return levels[1].remove(key);
		}
	}

	@Override
	protected void clearSync() throws IOException {
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
	public Iterator<Key> iterator() {
		return new Iterator<Key>() {
			private Iterator<Key> iterator0 = levels[0].iterator();
			private Iterator<Key> iterator1 = levels[1].iterator();
			@Override
			public boolean hasNext() {
				return iterator0.hasNext() || iterator1.hasNext();
			}

			@Override
			public Key next() {
				if (iterator0.hasNext()) {
					return iterator0.next();
				}
				else {
					return iterator1.next();
				}
			}
		};
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
		return levels[level].iterator();
	}
	@Override
	public int getCapacityOfLevel(int level) {
		return levels[level].getCapacity();
	}

	@Override
	public void setCapacityOfLevel(int level, int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Capacity can not be negative!");
		}
		levels[level].setCapacity(capacity);
		try {
			pushUpIfNeeded();
			pushDownIfNeeded();
		} catch (IOException e) {
			throw new RuntimeException("Internal error while changing capacity of cache: \n" + e.getMessage());
		}
	}

	@Override
	public int getSizeOfLevel(int level) {
		return levels[level].size();
	}

	// private methods

	private synchronized void pushUpIfNeeded() throws IOException {
		while (levels[0].size() < levels[0].getCapacity() && levels[1].size() != 0) {
			Pair<Key, Value> up = levels[1].pollFirst();
			levels[0].offerLast(up.getLeft(), up.getRight());
		}
	}

	private synchronized void pushDownIfNeeded() throws IOException {
		while (levels[0].size() > levels[0].getCapacity()) {
			Pair<Key, Value> down = levels[0].pollLast();
			levels[1].put(down.getLeft(), down.getRight());
		}
		while (levels[1].size() > levels[1].getCapacity()) {
			levels[1].pollLast();
		}
	}

	@Override
	public Collection<Key> keys() {
		List<Key> keySet = new LinkedList<>(levels[0].keys());
		keySet.addAll(levels[1].keys());
		return keySet;
	}
}
