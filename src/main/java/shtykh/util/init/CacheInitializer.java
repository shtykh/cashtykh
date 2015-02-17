package shtykh.util.init;

import shtykh.storage.cache.ICache;
import shtykh.storage.cache.TwoLevelCache;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shtykh on 16/02/15.
 */
public class CacheInitializer<Value extends Serializable> {
	private final ICache<String, Value> cache;
	private final ValueFactory<Value> valueFactory;

	public CacheInitializer(String text, ValueFactory<Value> valueFactory) throws IOException {
		this.valueFactory = valueFactory;
		cache = initGenesis(text);
	}

	private ICache<String, Value> initGenesis(String genesisString) throws IOException {
		String[] lines = genesisString.split("\n");
		int capacity  = lines.length * 100;
		int capacity0 = capacity / 40;
		int capacity1 = capacity - capacity0;
		ICache<String, Value> cache = new TwoLevelCache<>(capacity0, capacity1, true);
		for (int i = 0; i < lines.length; i ++) {
			String line = lines[i];
			Value value = valueFactory.make(String.valueOf(i), line);
			cache.put(String.valueOf(i), value);
		}
		return cache;
	}

	public ICache<String, Value>  get() {
		return cache;
	}
}
