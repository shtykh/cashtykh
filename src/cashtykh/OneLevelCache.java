package cashtykh;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

/**
 * Created by shtykh on 06/02/15.
 */
public class OneLevelCache<Value> implements ICache<String, Value> {
    private HashMap<String, Value> cache;


    @Override
    public Value get(String s) throws NoSuchElementException {
        if (null == cache) {
            throw new NoSuchElementException("Could not get an object with such key: " + s);
        }
        return cache.get(s);
    }

    @Override
    public void put(String s, Value o) {
        if (null == cache) {
            cache = new LinkedHashMap<>();
        }
        cache.put(s, o);
    }

    @Override
    public boolean remove(String s) {
        if (null == cache) {
            return false;
        }
        return null == cache.remove(s);
    }

    @Override
    public int size() {
        return null == cache ? 0 : cache.size();
    }
}
