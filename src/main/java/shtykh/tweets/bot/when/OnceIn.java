package shtykh.tweets.bot.when;

import org.apache.log4j.Logger;

import java.util.Date;

import static shtykh.tweets.bot.Bot.random;

/**
 * Created by shtykh on 29/03/15.
 */
public class OnceIn implements Longer {
	private static Logger log = Logger.getLogger(OnceIn.class);

	private final int daysMinimum;
	private final int daysMaximum;

	public OnceIn(int daysMinimum, int daysMaximum) {
		this.daysMinimum = daysMinimum;
		this.daysMaximum = daysMaximum;
	}

	@Override
	public long nextLong() {
		int minutesInDay = 1440;
		int minutes = random.nextInt((daysMaximum - daysMinimum) * minutesInDay) + daysMinimum * minutesInDay;
		long result = ((long) minutes) * 60 * 1000;
		log.info("Next action in " + new Date(System.currentTimeMillis() + result));
		return result;
	}
}
