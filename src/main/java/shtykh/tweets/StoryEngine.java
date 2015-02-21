package shtykh.tweets;

import shtykh.storage.cache.ICache;
import shtykh.tweets.tag.Tag;
import shtykh.tweets.tag.TagContext;
import shtykh.ui.UiUtil;
import shtykh.util.Story;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by shtykh on 11/02/15.
 */
public class StoryEngine {
	public static List<Tag> getHashTags(ICache<Tag, Story> storyCache, int queryCount) throws IOException {
		
		Collection<Tag> keys = storyCache.keys();
		for (Tag key : keys) {
			Story story = storyCache.getAndDoNotPutOnTop(key);
			if (story != null) {
				String[] wordsInStory = story.getStory().split("[ \n]");
				for (String word : wordsInStory) {
					if (word.startsWith("#")) {
						Tag.create(word);
					}
				}
			} else {
				UiUtil.showError(key + " - левый тэг", new NullPointerException(), null);
			}
		}
		
		return TagContext.getNMostFrequent(queryCount);
	}
}
