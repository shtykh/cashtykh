package shtykh.util.init;

/**
 * Created by shtykh on 17/02/15.
 */
public class StringFactory extends ValueFactory<String> {
	@Override
	public String make(String name, String line) {
		return line;
	}
}
