package cashtykh;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * Created by shtykh on 07/02/15.
 */
public interface Storage<Key, Value extends Serializable> {
	public Value get(Key key);

	public Value put(Key key, Value value);

	public Value remove(Key key);

	public void clear();
}
