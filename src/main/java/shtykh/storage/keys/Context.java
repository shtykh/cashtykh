package shtykh.storage.keys;

import java.util.List;

/**
 * Created by shtykh on 17/02/15.
 */
public class Context {
	private static volatile Context instance;
	private static Object monitor = new Object();
	
	private final Histogram<Tag> histogram;

	private Context() {
		histogram = new Histogram<>();
	}

	public static Context getInstance() {
		if (instance == null) {
			synchronized (monitor) {
				if (instance == null) {
					instance = new Context();
				}
			}
		}
		return instance;
	}
	
	public synchronized void register(Tag tag) {
		histogram.add(tag);
		tag.setFrequency(histogram.getFrequency(tag));
	}

	public List<Tag> getTagList() {
		return histogram.toSortedList();
	}
}
