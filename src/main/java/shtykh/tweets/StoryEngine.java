package shtykh.tweets;

import shtykh.storage.cache.ICache;
import shtykh.tweets.tag.Tag;
import shtykh.tweets.tag.TagContext;
import shtykh.ui.UiUtil;
import shtykh.util.Histogram;
import shtykh.util.Story;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by shtykh on 11/02/15.
 */
public class StoryEngine {
	private static Histogram<String> links = new Histogram<>();

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
						links.add(word);
					}
				}
			} else {
				UiUtil.showError(key + " - bad tag", new NullPointerException(), null);
			}
		}
		return TagContext.getNMostFrequent(queryCount);
	}

	public static List<String> getNMostFrequentLinks(int n) {
		return links.getNMostFrequent(n);
	}

	public static List<String> getAllFrequentLinks() {
		return links.getAllFrequentLinks();
	}
}
