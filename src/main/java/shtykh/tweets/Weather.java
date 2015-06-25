package shtykh.tweets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shtykh on 25/06/15.
 */
public class Weather {
	private final double humidity;
	private final double cloudcover;
	private final double feelsLikeC;
	private final double feelsLikeF;
	private final double temp_c;
	private final double temp_f;

	public Weather(double humidity,
				   double cloudcover,
				   double feelsLikeC,
				   double feelsLikeF,
				   double temp_C,
				   double temp_F) {
		this.humidity = humidity;
		this.cloudcover = cloudcover;
		this.feelsLikeC = feelsLikeC;
		this.feelsLikeF = feelsLikeF;
		temp_c = temp_C;
		temp_f = temp_F;
	}

	public static Weather readData(String jsonString) throws JSONException, TwitterAPIException {
		JSONObject jsonObject = new JSONObject(jsonString);
		try {
			JSONObject data = jsonObject.getJSONObject("data");
			JSONArray currentCondition = data.getJSONArray("current_condition");
			JSONObject currentCondition0 = (JSONObject) currentCondition.get(0);
			double humidity = currentCondition0.getDouble("humidity");
			double cloudcover = currentCondition0.getDouble("cloudcover");
			double FeelsLikeC = currentCondition0.getDouble("FeelsLikeC");
			double FeelsLikeF = currentCondition0.getDouble("FeelsLikeF");
			double temp_C = currentCondition0.getDouble("temp_C");
			double temp_F = currentCondition0.getDouble("temp_F");
			return new Weather(humidity, cloudcover, FeelsLikeC, FeelsLikeF, temp_C, temp_F);
		} catch (JSONException couldNotFindPlaces) {
			JSONArray errors;
			try {
				errors = jsonObject.getJSONArray("errors");
			} catch (JSONException ex) {
				throw couldNotFindPlaces;
			}
			throw new TwitterAPIException("\n" + errors.get(0).toString());
		}
	}

	public double getCloudcover() {
		return cloudcover;
	}

	public double getFeelsLikeC() {
		return feelsLikeC;
	}

	public double getFeelsLikeF() {
		return feelsLikeF;
	}

	public double getTemp_c() {
		return temp_c;
	}

	public double getTemp_f() {
		return temp_f;
	}

	public double getHumidity() {
		return humidity;
	}
}
