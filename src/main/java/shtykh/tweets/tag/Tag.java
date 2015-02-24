package shtykh.tweets.tag;

/**
 * Created by shtykh on 20/02/15.
 */
public class Tag implements Text {
	private String text;

	private int frequency;

	private Tag(String text, boolean add) {
		this.text = cleanText(text);
		if (add) {
			frequency = TagContext.incFrequency(this);
		} else {
			frequency = TagContext.getFrequency(this);
		}
	}

	@Override
	public int hashCode() {
		return text.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tag) {
			Tag tag = ((Tag) obj);
			return text.equals(tag.text);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return text + "(" + TagContext.getFrequency(this) + ")";
	}

	public static Tag get(String text) {
		return new Tag(text, false);
	}
	
	public static Tag create(String text) {
		return new Tag(text, true);
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getText() {
		return text;
	}

	public int getFrequency() {
		return frequency;
	}

	private String cleanText(String text) {
		String cleaned = text;
		for (String badSubString : new String[]{
				"!", "&", ",", ".", "$", "(", ")", "?", "'", "\"", ":", "@", ";", "&amp"}) {
			cleaned = cleaned.replace(badSubString, "");
		}
		return cleaned;
	}
}
