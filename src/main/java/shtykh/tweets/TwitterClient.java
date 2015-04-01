package shtykh.tweets;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import shtykh.tweets.frequent.Tag;
import shtykh.util.Story;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.List;

public class TwitterClient {
	private static Logger log = Logger.getLogger(TwitterClient.class);

	private OAuthConsumer consumer;
	private HttpClient client;

	public TwitterClient(Component parent) throws JSONException, IOException {
		String authData;
		try {
			authData = readFileAsString(System.getProperty("user.dir") + "/auth");
		} catch (IOException e) {
			String filePath = null;
			while (filePath == null) {
				filePath = getFileFromUser(parent);
			}
			authData = readFileAsString(filePath);
		}
		initConsumer(authData);
		client = new DefaultHttpClient();
	}

	private String getFileFromUser(Component parent) {
		String auth = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			auth = selectedFile.getAbsolutePath();
		}
		return auth;
	}

	private static String readFileAsString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead;
		while((numRead=reader.read(buf)) != -1){
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	private void initConsumer(String auth) throws JSONException {
		JSONObject authJson = new JSONObject(auth);
		String consumerKey = authJson.getString("consumerKey");
		String consumerSecret = authJson.getString("consumerSecret");
		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		String accessToken = authJson.getString("accessToken");
		String accessSecret = authJson.getString("accessSecret");
		consumer.setTokenWithSecret(accessToken, accessSecret);
	}

	public Tweets searchTweets(Tag query, int count) 
			throws OAuthException, 
				IOException, 
				JSONException, 
				TwitterAPIException {
		String cleanedQuery = query.getText().replace("#", "%23");
		HttpGet request = new HttpGet("https://api.twitter.com/1.1/search/tweets.json?q=" 
				+ cleanedQuery + 
				"&count=" + count);
		consumer.sign(request);
		HttpResponse response = client.execute(request);
		return new Tweets(query, IOUtils.toString(response.getEntity().getContent()));
	}
	
	public String post(Story tweet) throws TwitterAPIException {
		return post(tweet.getStory());
	}

	public synchronized String post(String tweet) throws TwitterAPIException {
		String tweetString = removeSpaces(tweet);
		HttpPost request = new HttpPost("https://api.twitter.com/1.1/statuses/update.json?status="
				+ tweetString);
		String result;
		try {
			consumer.sign(request);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			result = IOUtils.toString(entity.getContent());
		} catch (Exception e) {
			throw new TwitterAPIException(e);
		}
		log.info("Tweeting:");
		log.info(tweet);
		return result;
	}

	private Location whereIs(double _lat, double _long) throws TwitterAPIException, JSONException {
		HttpGet request = new HttpGet("https://api.twitter.com/1.1/geo/reverse_geocode.json?lat="
				+ _lat + "&long=" + _long);
		String entityString;
		try {
			consumer.sign(request);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			entityString = IOUtils.toString(entity.getContent());
		} catch (Exception e) {
			throw new TwitterAPIException(e);
		}
		List<Location> locations = Location.readPlaces(entityString);
		return locations.get(0);
	}

	private String findMyIp() throws IOException {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				whatismyip.openStream()));

		String ip = in.readLine();
		return ip;
	}

	public Location getLocation()
			throws JSONException,
			TwitterAPIException, 
			IOException {
		String myIp = findMyIp();
		HttpGet request = new HttpGet("http://freegeoip.net/json/" + myIp);
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String string = IOUtils.toString(entity.getContent());
		double[] coordinates = Location.readCoordinates(string);
		return whereIs(coordinates[0], coordinates[1]);
	}

	private String removeSpaces(String story) {
		String replace = story.replace(" ", "%20");
		replace = replace.replace("#", "%23");
		return replace;
	}
	
}