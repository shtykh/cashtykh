package shtykh.util.init;

import shtykh.util.Story;

/**
 * Created by shtykh on 17/02/15.
 */
public class StoryFactory extends Factory<Story> {
	@Override
	public Story make(String name, String line) {
		return new Story(name, line);
	}
}
