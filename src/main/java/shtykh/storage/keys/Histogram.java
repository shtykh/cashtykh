package shtykh.storage.keys;

import java.util.*;

import static java.lang.Math.signum;

/**
 * Created by shtykh on 17/02/15.
 */
public class Histogram<Key> implements SortedKeySet<Key> {
	private Map<Key, Integer> frequency = new HashMap<>();
	private Comparator<? super Key> frequencyComparator =
			(o1, o2) -> (int)signum(frequency.get(o2) - frequency.get(o1));

	@Override
	public boolean add(Key v) {
		if (frequency.containsKey(v)) {
			frequency.put(v, frequency.get(v) + 1);
		} else {
			frequency.put(v, 1);
		}
		return true;
	}
	
	@Override
	public List<Key> toSortedList() {
		List<Key> list = new ArrayList<>(frequency.keySet());
		Collections.sort(list, frequencyComparator);
		return list;
	}

	@Override
	public boolean offerFirst(Key key) {
		add(key);
		return true;
	}

	@Override
	public boolean offerLast(Key key) {
		add(key);
		return true;
	}

	@Override
	public int size() {
		return frequency.size();
	}

	@Override
	public boolean contains(Key key) {
		return frequency.containsKey(key);
	}

	@Override
	public boolean remove(Key key) {
		return null != frequency.remove(key);
	}

	@Override
	public int aBeforeB(Key a, Key b) {
		return frequencyComparator.compare(a, b);
	}

	public int getFrequency(Key key) {
		return frequency.get(key);
	}
}
