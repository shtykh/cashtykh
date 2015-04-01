package shtykh.tweets.frequent;

import java.util.Comparator;

/**
 * Created by shtykh on 24/02/15.
 */
public interface Frequent {
	public static Comparator<Frequent> comparator = new Comparator<Frequent>() {
		@Override
		public int compare(Frequent o1, Frequent o2) {
			return (int) Math.signum(o1.getFrequency() - o2.getFrequency());
		}
	};
	void setFrequency(int frequency);
	int getFrequency();
}
