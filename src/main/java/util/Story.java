package util;

import java.io.Serializable;

/**
 * Created by shtykh on 07/02/15.
 */
public class Story implements Serializable{
	private final String title;
	private final String story;
	public static String DEFAULT_STORY = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " + "\n" +
			"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " + "\n" +
			"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " + "\n" +
			"ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " + "\n" +
			"in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " + "\n" +
			"Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " + "\n" +
			"officia deserunt mollit anim id est laborum.";

	public Story(String title) {
		this(title, DEFAULT_STORY);
	}

	public Story(String title, String story) {
		this.title = title;
		this.story = story;
	}

	public String toString() {
		return title + ": " + story;
	}

	public String getTitle() {
		return title;
	}
}
