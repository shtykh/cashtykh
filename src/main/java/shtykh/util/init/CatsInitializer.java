package shtykh.util.init;

import shtykh.tweets.tag.Tag;
import shtykh.util.Story;

import java.io.IOException;

/**
 * Created by shtykh on 17/02/15.
 */
public class CatsInitializer extends CacheInitializer<Tag, Story> {
	private static final String CATS_STRING = "#кот #котик #котёнок #кошка";

	public CatsInitializer() throws IOException {
		super(CATS_STRING, new TagFactory(), new StoryFactory());
	}
}
