package cashtykh;


import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;

/**
 * Created by shtykh on 06/02/15.
 */
public interface ICache<Key, Value extends Serializable> extends Storage<Key, Value>{
	public int getCapacity();

	public boolean containsKey(Key key);
	public Value offerLast(Key key, Value value);
	public Pair<Key, Value> pollLast();
	public Pair<Key, Value> pollFirst();
	public int size();
}
