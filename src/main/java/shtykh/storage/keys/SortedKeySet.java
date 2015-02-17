package shtykh.storage.keys;

import java.util.Iterator;
import java.util.List;

/**
 * Created by shtykh on 17/02/15.
 */
public interface SortedKeySet<Key> {
	boolean add(Key v);

	default List<Key> getNFirst(int n) {
		List<Key> keys = toSortedList();
		if (keys.size() > n) {
			keys = keys.subList(0, n - 1);
		}
		return keys;
	}

	default List<Key> getNLast(int n) {
		List<Key> keys = toSortedList();
		if (keys.size() > n) {
			keys = keys.subList(keys.size() - n, keys.size() - 1);
		}
		return keys;
	}

	int size();

	boolean offerFirst(Key key);

	boolean offerLast(Key key);

	default public Key pollLast() {
		List<Key> list = toSortedList();
		Key last = list.get(list.size() - 1);
		remove(last);
		return last;
	}

	default public Key pollFirst() {
		Key first = toSortedList().get(0);
		remove(first);
		return first;
	}

	default public Iterator<Key> iterator() {
		return toSortedList().iterator();
	}

	boolean contains(Key key);

	boolean remove(Key key);

	List<Key> toSortedList();

	int aBeforeB(Key a, Key b);
}
