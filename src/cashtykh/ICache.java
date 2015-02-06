package cashtykh;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by shtykh on 06/02/15.
 */
public interface ICache<Key, Value> {
    public Value retrieve(Key key) throws NoSuchElementException;

	public void cache(Key key, Value value);

    /**
     *
     * @param key - the key of object to delete;
     * @return <tt>true<tt/> if and only if entity corresponding to the key was successfully removed
     */
    public boolean delete(Key key);

    public int size();

	public Iterator<Key> firstLevelIterator();

	public Iterator<Key> secondLevelIterator();


}
