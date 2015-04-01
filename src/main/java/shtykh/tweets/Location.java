package shtykh.tweets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shtykh on 12/03/15.
 */
public class Location {
	private final String id;
	private final String name;
	private final String fullName;
	private final String placeType;
	private final String countryCode;
	
	public Location(String id, String name, String fullName, String placeType, String countryCode) {
		this.id = id;
		this.name = name;
		this.fullName = fullName;
		this.placeType = placeType;
		this.countryCode = countryCode;
	}

	public static double[] readCoordinates(String jsonString) throws JSONException, TwitterAPIException {
		JSONObject jsonObject = new JSONObject(jsonString);
		double longitude;
		double latitude;
		try {
			latitude = jsonObject.getDouble("latitude");
			longitude = jsonObject.getDouble("longitude");
		} catch (JSONException couldNotFindPlaces) {
			JSONArray errors;
			try {
				errors = jsonObject.getJSONArray("errors");
			} catch (JSONException ex) {
				throw couldNotFindPlaces;
			}
			throw new TwitterAPIException("\n" + errors.get(0).toString());
		}
		return new double[]{latitude, longitude};
	}

	public static List<Location> readPlaces(String jsonString) throws JSONException, TwitterAPIException {
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONArray places;
		try {
			JSONObject result = jsonObject.getJSONObject("result");
			places = result.getJSONArray("places");
		} catch (JSONException couldNotFindPlaces) {
			JSONArray errors;
			try {
				errors = jsonObject.getJSONArray("errors");
			} catch (JSONException ex) {
				throw couldNotFindPlaces;
			}
			throw new TwitterAPIException("\n" + errors.get(0).toString());
		}
		List<Location> placesList = new ArrayList<>(places.length());
		for (int i = 0; i < places.length(); i++) {
			JSONObject place = places.getJSONObject(i);
			String id = place.getString("id");
			String name = place.getString("name");
			String fullName = place.getString("full_name");
			String placeType = place.getString("place_type");
			String countryCode = place.getString("country_code");
			placesList.add(new Location(id, name, fullName, placeType, countryCode));
		}
		return placesList;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return fullName;
	}

	public String getPlaceType() {
		return placeType;
	}

	public String getCountryCode() {
		return countryCode;
	}
}
