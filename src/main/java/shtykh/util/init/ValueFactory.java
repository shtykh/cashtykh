package shtykh.util.init;

import java.io.Serializable;

/**
 * Created by shtykh on 17/02/15.
 */
public abstract class ValueFactory<Value extends Serializable> {
	public abstract Value make(String name, String line);
}
