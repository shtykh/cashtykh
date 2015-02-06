package cashtykh;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

/**
 * Created by shtykh on 06/02/15.
 */
public class OneLevelCache<Value> extends LinkedHashMap<String, Value> implements ICache<String, Value> {

	@Override
	public Value get(String s) throws NoSuchElementException {
		return super.get(s);
	}

	@Override
	public boolean remove(String s) {
		return null != super.remove(s);
	}
}
