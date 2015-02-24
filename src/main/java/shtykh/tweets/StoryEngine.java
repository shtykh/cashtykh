package shtykh.tweets;

import shtykh.storage.cache.ICache;
import shtykh.tweets.frequent.Context;
import shtykh.tweets.frequent.Link;
import shtykh.tweets.frequent.Tag;
import shtykh.ui.UiUtil;
import shtykh.util.Story;

import java.io.IOException;
import java.net.URISyntaxException;
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
					if (word.startsWith("http") && !word.endsWith("…")) {
						try {
							Link.create(word);
						} catch (URISyntaxException ignored) {
						}
					}
				}
			} else {
				UiUtil.showError(key + " - bad frequent", new NullPointerException(), null);
			}
		}
		return Context.getNMostFrequent(queryCount, Tag.class);
	}
}
