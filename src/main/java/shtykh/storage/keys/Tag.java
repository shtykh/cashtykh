package shtykh.storage.keys;

/**
 * Created by shtykh on 17/02/15.
 */
public class Tag {
	private final String text;
	private int frequency;

	public Tag(String text) {
		this.text = text;
		Context.getInstance().register(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tag tag = (Tag) o;

		if (text != null ? !text.equals(tag.text) : tag.text != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return text != null ? text.hashCode() : 0;
	}

	@Override
	public String toString() {
		return text + " (" + frequency + ")";
	}

	public String getText() {
		return text;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}
