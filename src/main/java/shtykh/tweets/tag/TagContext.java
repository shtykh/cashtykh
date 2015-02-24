package shtykh.tweets.tag;

import shtykh.util.Histogram;

import java.util.List;

/**
 * Created by shtykh on 20/02/15.
 */
public class TagContext {
	private static Object mutex = new Object();
	private static TagContext instance;
	private final Histogram<Tag> tagCloud;

	private TagContext() {
		tagCloud = new Histogram<>();
	}
	
	public static TagContext getInstance() {
		if (instance == null) {
			synchronized (mutex) {
				if (instance == null) {
					instance = new TagContext();
				}
			}
		}
		return instance;
	}
	
	public synchronized static int incFrequency(Tag tag) {
		TagContext instance = getInstance();
		instance.tagCloud.add(tag);
		return instance.tagCloud.getFrequency(tag);
	}

	public static int getFrequency(Tag tag) {
		return getInstance().tagCloud.getFrequency(tag);
	}

	public static List<Tag> getNMostFrequent(int n) {
		return getInstance().tagCloud.getNMostFrequent(n);
	}

}
