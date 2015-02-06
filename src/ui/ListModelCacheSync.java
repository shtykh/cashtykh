package ui;

import cashtykh.ICache;

import javax.swing.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class ListModelCacheSync extends DefaultListModel {
	private final ICache cache;

	public ListModelCacheSync(ICache cache) {
		super();
		this.cache = cache;
	}

	public void push(Object obj, Object value) {
		cache.put(obj, value);
		super.add(0, obj);
	}

	public boolean removeElement(int index) {
		Object obj = get(index);
		cache.remove(obj);
		return super.removeElement(obj);
	}
}
