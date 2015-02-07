package cashtykh;

import com.sun.tools.javac.util.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by shtykh on 06/02/15.
 */
public interface ICache<Key, Value> extends Storage<Key, Value>{
	public int getCapacity();


	public Value offerLast(Key key, Value value);
	public Pair<Key, Value> pollLast();
	public Pair<Key, Value> pollFirst();
}
