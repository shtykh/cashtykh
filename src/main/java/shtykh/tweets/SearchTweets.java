package shtykh.tweets;

import shtykh.task.Receiver;
import shtykh.task.Task;
import shtykh.tweets.frequent.Tag;

/**
 * Created by shtykh on 11/02/15.
 */
public class SearchTweets extends Task<Tweets> {
	private final TwitterClient client;
	private final Tag query;
	private final int twitsCount;

	public SearchTweets(TwitterClient client, Receiver<Tweets> receiver, Tag query, int twitsCount) {
		this(client, receiver, query, twitsCount, true);
	}

	public SearchTweets(TwitterClient client, 
						Receiver<Tweets> receiver,
						Tag query,
						int twitsCount, 
						boolean visible) {
		super(receiver, true, visible);
		this.client = client;
		this.query = query;
		this.twitsCount = twitsCount;
	}

	@Override
	protected Tweets doInBackground() throws Exception {
		publish("Searching " + twitsCount + " tweets by query : " + query);
		setProgress(1);
		Tweets tweets = client.searchTweets(query, twitsCount);
		setProgress(100);
		return tweets;
	}
}
