package shtykh.storage.keys;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shtykh on 17/02/15.
 */
public class Latergram<Key> extends LinkedList<Key> implements SortedKeySet<Key> {

	@Override
	public List<Key> toSortedList() {
		return this;
	}

	@Override
	public int aBeforeB(Key a, Key b) {
		return this.indexOf(b) - this.indexOf(a);
	}

	@Override
	public boolean offerFirst(Key key) {
		remove(key);
		return super.offerFirst(key);
	}

	@Override
	public boolean offerLast(Key key) {
		remove(key);
		return super.offerLast(key);
	}
}
