package shtykh.util;

import java.util.*;

import static java.lang.Math.signum;

/**
 * Created by shtykh on 17/02/15.
 */
public class Histogram<Key>{
	private Map<Key, Integer> frequency = new HashMap<>();
	private Comparator<? super Key> frequencyComparator =
			(o1, o2) -> (int)signum(frequency.get(o2) - frequency.get(o1));

	public void add(Key v) {
		if (!frequency.containsKey(v)) {
			frequency.put(v, 1);
			return;
		} else {
			frequency.put(v, frequency.get(v) + 1);
		}
	}
	
	public List<Key> getNMostFrequent(int n) {
		List<Key> keys = new ArrayList<>(frequency.keySet());
		Collections.sort(keys, frequencyComparator);
		if (keys.size() > n) {
			keys = keys.subList(0, n);
		}
		return keys;
	}

	public int getFrequency(Key key) {
		return frequency.containsKey(key) ? frequency.get(key) : 0;
	}

	public boolean contains(Key key) {
		return frequency.containsKey(key);
	}

	public List<Key> getAllFrequentLinks() {
		return getNMostFrequent(frequency.size());
	}
}
