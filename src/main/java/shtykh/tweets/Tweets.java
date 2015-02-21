package shtykh.tweets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import shtykh.tweets.tag.Tag;
import shtykh.util.Story;

/**
 * Created by shtykh on 10/02/15.
 */
public class Tweets extends Story {
	public Tweets(Tag tag, String content) throws JSONException, TwitterAPIException {
		super(tag.getText(), getBody(tag.getText(), content));
	}

	private static String getBody(String title, String content) throws JSONException, TwitterAPIException {
		JSONObject jsonObject = new JSONObject(content);
		JSONArray statuses;
		try {
			statuses = jsonObject.getJSONArray("statuses");
		} catch (JSONException couldNotFindStatuses) {
			JSONArray errors;
			try {
				errors = jsonObject.getJSONArray("errors");
			} catch (JSONException ex) {
				throw couldNotFindStatuses;
			}
			throw new TwitterAPIException(title + "\n" + errors.get(0).toString());
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < statuses.length(); i++) {
			JSONObject status = statuses.getJSONObject(i);
			JSONObject user = status.getJSONObject("user");
			sb.append("@").append(user.getString("screen_name")).append(" : ");
			sb.append(status.getString("text")).append("\n");
		}
		return sb.toString();
	}
}
