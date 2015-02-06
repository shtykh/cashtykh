package ui;

import cashtykh.ICache;

import javax.swing.*;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by shtykh on 06/02/15.
 */
public class ListModelCacheSync extends DefaultListModel {
	private final ICache cache;
	private boolean firstLevel;

	public ListModelCacheSync(ICache cache, boolean firstLevel) {
		super();
		this.cache = cache;
		this.firstLevel = firstLevel;
	}

	public void push(Object obj, Object value) {
		cache.cache(obj, value);
		sync();
	}

	public void removeElement(int index) {
		Object obj = get(index);
		cache.delete(obj);
		sync();
	}

	public void sync() {
		super.clear();
		Iterator keyIterator = firstLevel ? cache.firstLevelIterator() : cache.secondLevelIterator();
		keyIterator.forEachRemaining(obj -> super.addElement(obj));
	}
}
