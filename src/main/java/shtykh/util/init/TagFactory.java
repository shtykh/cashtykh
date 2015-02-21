package shtykh.util.init;

import shtykh.tweets.tag.Tag;

/**
 * Created by shtykh on 20/02/15.
 */
public class TagFactory extends Factory<Tag> {
	@Override
	public Tag make(String name, String line) {
		return Tag.get(line);
	}
}
