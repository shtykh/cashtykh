package shtykh.util.init;

import shtykh.storage.cache.ICache;
import shtykh.storage.cache.TwoLevelCache;
import shtykh.storage.Text;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shtykh on 16/02/15.
 */
public class CacheInitializer<Key extends Text, Value extends Serializable> {
	private final ICache<Key, Value> cache;
	private final Factory<Key> keyFactory;
	private final Factory<Value> valueFactory;

	public CacheInitializer(String text, Factory<Key> keyFactory, Factory<Value> valueFactory) throws IOException {
		this.keyFactory = keyFactory;
		this.valueFactory = valueFactory;
		cache = initCache(text);
	}

	private ICache<Key, Value> initCache(String text) throws IOException {
		String[] lines = text.split("\n");
		int capacity  = lines.length * 100;
		int capacity0 = capacity / 40;
		int capacity1 = capacity - capacity0;
		ICache<Key, Value> cache = new TwoLevelCache<>(capacity0, capacity1, true);
		for (int i = 0; i < lines.length; i ++) {
			String line = lines[i];
			String name = String.valueOf(i);
			Value value = valueFactory.make(name, line);
			cache.put(keyFactory.make(name, name), value);
		}
		return cache;
	}

	public ICache<Key, Value>  get() {
		return cache;
	}
}
