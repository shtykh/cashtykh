package ui;

import cashtykh.ICache;
import cashtykh.TwoLevelCache;

import javax.swing.*;
import java.util.Iterator;

/**
 * Created by shtykh on 06/02/15.
 */
public class ListModelCacheSync extends DefaultListModel {
	private final TwoLevelCache cache;
	private boolean firstLevel;

	public ListModelCacheSync(TwoLevelCache cache, boolean firstLevel) {
		super();
		this.cache = cache;
		this.firstLevel = firstLevel;
	}

	public void push(Object obj, Object value) {
		cache.put(obj, value);
		sync();
	}

	public void removeElement(int index) {
		Object obj = get(index);
		cache.remove(obj);
		sync();
	}

	public void sync() {
		super.clear();
		Iterator keyIterator = firstLevel ? cache.firstLevelIterator() : cache.secondLevelIterator();
		keyIterator.forEachRemaining(obj -> super.addElement(obj));
	}
}
