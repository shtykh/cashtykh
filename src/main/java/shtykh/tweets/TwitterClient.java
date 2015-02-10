package shtykh.tweets;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TwitterClient {

	private OAuthConsumer consumer;

	public TwitterClient(Component parent) throws JSONException, IOException {
		String authData = null;
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
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(
				new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead=0;
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

	public Tweets searchTweets(String query) throws OAuthException, IOException, JSONException {
		return searchTweets(query, 15);
	}

	public Tweets searchTweets(String query, int count) throws OAuthException, IOException, JSONException {
		HttpGet request = new HttpGet("https://api.twitter.com/1.1/search/tweets.json?q=" + query + "&count=" + count);
		consumer.sign(request);

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		return new Tweets(query, IOUtils.toString(response.getEntity().getContent()));
	}
}