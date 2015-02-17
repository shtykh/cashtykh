package shtykh.tweets;

import shtykh.storage.cache.ICache;
import shtykh.util.Story;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by shtykh on 11/02/15.
 */
public class StoryEngine {
	public static List<String> getHashTags(ICache<String, Story> storyCache, int queryCount) throws IOException {
		List<String> words = new ArrayList<>();
		Collection<String> keys = storyCache.keys();
		for (String key : keys) {
			String[] wordsInStory = storyCache.getAndDoNotPutOnTop(key).toString().split("[ \n]");
			for (String word : wordsInStory) {
				if (word.startsWith("#")) {
					words.add(word);
					if (words.size() == queryCount) {
						return words;
					}
				}
			}
		}
		if (words.size() > queryCount) {
			words = words.subList(0, queryCount - 1);
		}
		return words;
	}
}
