package shtykh.util;

import java.util.*;

import static java.lang.Math.*;

/**
 * Created by shtykh on 17/02/15.
 */
public class Histogram<Value>{
	private Map<Value, Integer> frequency = new HashMap<>();
	private Comparator<? super Value> frequencyComparator = 
			(o1, o2) -> (int)signum(frequency.get(o2) - frequency.get(o1));

	public void add(Value v) {
		if (!frequency.containsKey(v)) {
			frequency.put(v, 1);
			return;
		} else {
			frequency.put(v, frequency.get(v) + 1);
		}
	}
	
	public List<Value> getNMostFrequent(int n) {
		List<Value> values = new ArrayList<>(frequency.keySet());
		Collections.sort(values, frequencyComparator);
		if (values.size() > n) {
			values = values.subList(0, n - 1);
		}
		return values;
	}
}
