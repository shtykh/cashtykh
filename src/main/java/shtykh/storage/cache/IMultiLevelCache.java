package shtykh.storage.cache;

import shtykh.tweets.tag.IKey;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by shtykh on 09/02/15.
 */
public interface IMultiLevelCache<Key, Value extends Serializable> extends ICache<Key, Value> {
	public Iterator<Key> keyIteratorOfLevel(int level);
	public int getCapacityOfLevel(int level);
	public void setCapacityOfLevel(int level, int capacity);

	public int getSizeOfLevel(int level);
}
