package shtykh.tweets;

import shtykh.task.Receiver;
import shtykh.task.Task;

/**
 * Created by shtykh on 11/02/15.
 */
public class SearchTweets extends Task<Tweets> {
	private final TwitterClient client;
	private final String querry;
	private final int twitsCount;

	public SearchTweets(TwitterClient client, Receiver<Tweets> receiver, String query, int twitsCount) {
		super(receiver);
		this.client = client;
		this.querry = query;
		this.twitsCount = twitsCount;
	}

	@Override
	protected Tweets doInBackground() throws Exception {
		publish("Searching tweets by query : " + querry);
		setProgress(1);
		Tweets tweets = client.searchTweets(querry, twitsCount);
		setProgress(100);
		return tweets;
	}
}
