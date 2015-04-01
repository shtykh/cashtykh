package shtykh.tweets.bot;


import org.apache.log4j.Logger;
import shtykh.tweets.TwitterClient;
import shtykh.tweets.bot.what.Stringer;
import shtykh.tweets.bot.when.Longer;

/**
 * Created by shtykh on 29/03/15.
 */
public class Parrot extends Thread {
	private final Stringer what;
	private final Longer when;
	private final Booleaner ifWhat;
	private final TwitterClient tc;
	private static Logger log = Logger.getLogger(Parrot.class);

	public Parrot(Stringer what,
				  Longer when,
				  Booleaner ifWhat,
				  TwitterClient tc) {
		super();
		this.what = what;
		this.when = when;
		this.ifWhat = ifWhat;
		this.tc = tc;
		setDaemon(true);
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (ifWhat.nextBoolean()) {
					tc.post(what.nextString());
				} else {
					log.info("Not tweeting");
				}
				Thread.sleep(when.nextLong());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
