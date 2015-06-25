package shtykh.tweets.bot;

import org.junit.Test;
import shtykh.tweets.TwitterClient;

public class PtichkaTest {

	@Test
	public void testNextString() throws Exception {
		System.out.println(new TwitterClient(null).getWeather("State+College").getHumidity());
	}
}