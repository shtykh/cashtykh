package cashtykh;

import java.util.NoSuchElementException;

/**
 * Created by shtykh on 07/02/15.
 */
public interface Storage<Key, Value> {
	public Value get(Key key) throws NoSuchElementException;

	public Value put(Key key, Value value);

	public boolean containsKey(Key key);

	/**
	 *
	 * @param key - the key of object to remove;
	 * @return <tt>true<tt/> if and only if entity corresponding to the key was successfully removed
	 */
	public Value remove(Key key);

	public int size();
}
