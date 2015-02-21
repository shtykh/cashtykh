package shtykh.storage;

import shtykh.tweets.tag.IKey;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shtykh on 07/02/15.
 */
public interface Storage<Key, Value extends Serializable> {
	public Value get(Key key) throws IOException;
	public Value put(Key key, Value value) throws IOException;
	public Value remove(Key key) throws IOException;
	public void clear() throws IOException;
	
}
