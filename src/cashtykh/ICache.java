package cashtykh;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by shtykh on 06/02/15.
 */
public interface ICache<Key, Value> extends Iterable<Key>{
    public Value retrieve(Key key) throws NoSuchElementException;

	public void cache(Key key, Value value);

    /**
     *
     * @param key - the key of object to delete;
     * @return <tt>true<tt/> if and only if entity corresponding to the key was successfully removed
     */
    public Value delete(Key key);

    public int size();

}
