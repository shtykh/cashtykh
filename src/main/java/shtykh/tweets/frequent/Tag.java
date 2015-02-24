package shtykh.tweets.frequent;

/**
 * Created by shtykh on 20/02/15.
 */
public class Tag extends FrequentText {
	private Tag(String text, boolean add) {
		super(cleanText(text), add);
	}

	public static Tag get(String text) {
		return new Tag(text, false);
	}
	
	public static Tag create(String text) {
		return new Tag(text, true);
	}

	private static String cleanText(String text) {
		String cleaned = text;
		for (String badSubString : new String[]{
				"!", "&", ",", ".", "$", "(", ")", "?", "'", "\"", ":", "@", ";", "&amp"}) {
			cleaned = cleaned.replace(badSubString, "");
		}
		return cleaned;
	}
}
