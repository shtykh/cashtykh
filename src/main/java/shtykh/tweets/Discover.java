package shtykh.tweets;

import com.sun.istack.internal.NotNull;
import shtykh.storage.cache.ICache;
import shtykh.task.Receiver;
import shtykh.task.Task;
import shtykh.util.Story;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shtykh on 11/02/15.
 */
public class Discover extends Task<Tweets> implements Receiver<Tweets> {
	private static final int PERCENTS_TO_FIND_HASH_TAGS = 30;
	
	private final TwitterClient client;
	private final ICache<String, Story> storyCache;
	private int queryCount;

	private final int twitsInQuery;
	private final int iterations;
	private AtomicInteger jobsDoneCount = new AtomicInteger(0);

	public Discover(@NotNull TwitterClient client,
					Receiver<Tweets> receiver,
					@NotNull ICache<String, Story> storyCache,
					int queryCount,
					int twitsInQuery, 
					int iterations) {
		super(receiver, false, true);
		this.client = client;
		this.storyCache = storyCache;
		this.queryCount = queryCount;
		this.twitsInQuery = twitsInQuery;
		this.iterations = iterations;
	}

	@Override
	protected Tweets doInBackground() throws Exception {
		publish("Discovering tweets");
		for (int iteration = 0; iteration < iterations; iteration ++) {
			jobsDoneCount.set(0);
			publish("Iteration " + iteration);
			publish("Searching for hashtags...");
			setProgress(1);
			List<String> hashTags = StoryEngine.getHashTags(storyCache, queryCount);
			publish("Hashtags found:");
			for (String word: hashTags) {
				publish(word);
			}
			queryCount = Math.min(hashTags.size(), queryCount);
			setProgress(PERCENTS_TO_FIND_HASH_TAGS);
			for (int i = 0; i < queryCount; i++) {
				String hashTag = hashTags.get(i);
				if (storyCache.containsKey(hashTag)) {
					setJobDone(hashTag + " was skipped");
				} else {
					SearchTweets searchTweets = new SearchTweets(client, this, hashTag, twitsInQuery, false);
					searchTweets.start();
				}
			}
			while (jobsDoneCount.get() < queryCount) {
				Thread.sleep(1000);
			}	
		}
		return null;
	}

	@Override
	public void onReceive(Tweets tweets) {
		getReceiver().onReceive(tweets);
		setJobDone("Tweets by querry \"" + tweets.getTitle() + "\" are loaded");
	}

	private void setJobDone(String msg) {
		publish(msg);
		jobsDoneCount.getAndIncrement();
		double proportionDone = ((double) jobsDoneCount.get()) / (queryCount);
		double doublePercent = PERCENTS_TO_FIND_HASH_TAGS + proportionDone * (100 - PERCENTS_TO_FIND_HASH_TAGS);
		int percent = (int) doublePercent;
		setProgress(percent);
	}
}
