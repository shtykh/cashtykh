package cashtykh;

import java.util.NoSuchElementException;

/**
 * Created by shtykh on 06/02/15.
 */
public interface ICache<Key, Value> {
    public Value get(Key key) throws NoSuchElementException;

	public void put(Key key, Value value);

    /**
     *
     * @param key - the key of object to remove;
     * @return <tt>true<tt/> if and only if entity corresponding to the key was successfully removed
     */
    public boolean remove(Key key);

    public int size();
}
