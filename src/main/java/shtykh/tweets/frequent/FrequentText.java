package shtykh.tweets.frequent;

import shtykh.storage.Text;

/**
 * Created by shtykh on 20/02/15.
 */
public class FrequentText implements Frequent, Text {
	private String text;
	private int frequency;

	public FrequentText(String text, boolean add) {
		this.text = text;
		if (add) {
			frequency = Context.incFrequency(this);
		} else {
			frequency = Context.getFrequency(this);
		}
	}

	@Override
	public int hashCode() {
		return text.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FrequentText) {
			FrequentText frequentText = ((FrequentText) obj);
			return text.equals(frequentText.text);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return text + "(" + Context.getFrequency(this) + ")";
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public String getText() {
		return text;
	}
}
