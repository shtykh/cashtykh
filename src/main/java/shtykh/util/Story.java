package shtykh.util;

import java.io.Serializable;

/**
 * Created by shtykh on 07/02/15.
 */
public class Story implements Serializable{
	private final String title;
	private final String story;

	public Story(String title, String story) {
		this.title = title;
		this.story = story;
	}

	public String getTitle() {
		return title;
	}

	public String toString() {
		return story;
	}
}
