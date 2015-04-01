package shtykh.tweets.bot.when;

import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by shtykh on 29/03/15.
 */
public class Daily implements Longer {
	private static Logger log = Logger.getLogger(Daily.class);
	
	@Override
	public long nextLong() {
		long result = 1440 * 60 * 1000;
		log.info("Next action in " + new Date(System.currentTimeMillis() + result));
		return result;
	}
}
