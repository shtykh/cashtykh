package shtykh.tweets;

import shtykh.storage.cache.ICache;
import shtykh.util.Histogram;
import shtykh.util.Story;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by shtykh on 11/02/15.
 */
public class StoryEngine {
	public static List<String> getHashTags(ICache<String, Story> storyCache, int queryCount) throws IOException {
		Histogram<String> hashtags = new Histogram<>();
		Collection<String> keys = storyCache.keys();
		for (String key : keys) {
			String[] wordsInStory = storyCache.getAndDoNotPutOnTop(key).toString().split("[ \n]");
			for (String word : wordsInStory) {
				if (word.startsWith("#")) {
					hashtags.add(word);
				}
			}
		}
		return hashtags.getNMostFrequent(queryCount);
	}
}
