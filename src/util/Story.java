package util;

/**
 * Created by shtykh on 07/02/15.
 */
public class Story {
	private String title;

	public Story(String title) {
		this.title = title;
	}

	public String toString() {
		return title + ": Lorem ipsum dolor sit amet, consectetur adipiscing elit, " + "\n" +
				"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " + "\n" +
				"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " + "\n" +
				"ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " + "\n" +
				"in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " + "\n" +
				"Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " + "\n" +
				"officia deserunt mollit anim id est laborum.";
	}
}
