package shtykh.tweets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import shtykh.util.Story;

/**
 * Created by shtykh on 10/02/15.
 */
public class Tweets extends Story {
	public Tweets(String title, String content) throws JSONException {
		super(title, getBody(content));
	}

	private static String getBody(String content) throws JSONException {
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
			throw new JSONException(((JSONObject)errors.get(0)).getString("message"));
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
