package shtykh.storage.cache;

import shtykh.storage.Storage;
import java.io.Serializable;

/**
 * Created by shtykh on 06/02/15.
 */
public interface ICache<Key, Value extends Serializable> extends Storage<Key, Value> {
	public int getCapacity();
	public boolean containsKey(Key key);
	public int size();
}
