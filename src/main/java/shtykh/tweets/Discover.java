package shtykh.tweets;

import shtykh.storage.cache.ICache;
import shtykh.task.Receiver;
import shtykh.task.Task;
import shtykh.util.Story;

import java.util.List;

/**
 * Created by shtykh on 11/02/15.
 */
public class Discover extends Task<Tweets> {
	private final TwitterClient client;
	private final ICache<String, Story> storyCache;
	private final int queryCount;
	private final int twitsCount;

	public Discover(TwitterClient client, Receiver<Tweets> receiver, ICache<String, Story> storyCache, int queryCount, int twitsCount) {
		super(receiver);
		this.client = client;
		this.storyCache = storyCache;
		this.queryCount = queryCount;
		this.twitsCount = twitsCount;
	}

	@Override
	protected Tweets doInBackground() throws Exception {
		publish("Discovering tweets");
		setProgress(1);
		List<String> frequentWords = StoryEngine.getFrequentWords(storyCache, queryCount);
		int percentsToFindFrequentWords = 60;
		setProgress(percentsToFindFrequentWords);
		for (int i = 0; i < frequentWords.size(); i++) {
			SearchTweets searchTweets = new SearchTweets(client, getReceiver(), frequentWords.get(i), twitsCount, false);
			int percent = (int) (percentsToFindFrequentWords + new Double(i) / (frequentWords.size()) * (100 - percentsToFindFrequentWords));
			searchTweets.start();
			setProgress(percent);
		}

		setProgress(100);
		return null;
	}
	
}
