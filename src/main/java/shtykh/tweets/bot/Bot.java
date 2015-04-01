package shtykh.tweets.bot;

import org.apache.log4j.Logger;
import org.json.JSONException;
import shtykh.tweets.Location;
import shtykh.tweets.TwitterAPIException;
import shtykh.tweets.TwitterClient;
import shtykh.tweets.bot.what.Ptichka;
import shtykh.tweets.bot.when.OnceIn;
import shtykh.ui.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by shtykh on 29/03/15.
 */
public class Bot extends JFrame {
	public static Random random = new Random();
	private static Logger log = Logger.getLogger(Bot.class);
	
	private TwitterClient twitterClient;
	private Location lastLocation;
	private Ptichka ptichka;
	private FoodParrot foodParrot;
	
	public Bot() throws HeadlessException, IOException, JSONException {
		twitterClient = new TwitterClient(this);
		ptichka = new Ptichka();
		foodParrot = new FoodParrot(twitterClient);
		
	}

	private void checkLocation() throws TwitterAPIException, JSONException, IOException {
		synchronized (twitterClient) {
			Location location = twitterClient.getLocation();
			if (location == null) {
				return;
			}
			if (!location.equals(lastLocation)) {
				twitterClient.post(location.getName() + ", я в тебе");
				lastLocation = location;
			}
		}
	}

	private void checkVoditchki() throws TwitterAPIException {
		int i = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		double probability = 0;
		switch (i) {
			//суббота-воскресенье
			case 1:
			case 7:
				probability = 0.7;
				break;
			default:
				probability = 0.2;
		}
		double nextDouble = random.nextDouble();
		if (nextDouble < probability) {
			twitterClient.post("#водички");
		} else {
			log.info("not tweeting wodichki");
		}
	}

	private void checkPtichka() throws TwitterAPIException {
		if (!ptichka.nextBoolean()) {
			twitterClient.post(ptichka.nextString());
			return;
		}
		if (random.nextDouble() < 0.1) {
			int numberOfSweets = random.nextInt(20) + 20;
			ptichka.add(numberOfSweets);
		} else {
			log.info("No new sweets party :(");
		}
	}

	public static void main(String[] args)
			throws TwitterAPIException, JSONException, IOException, InterruptedException {
		Bot bot = null;
		try {
			bot = new Bot();
		} catch (IOException | JSONException e) {
			UiUtil.showError("Ошибка при инициализации бота", e, null);
		}
		final Bot finalBot = bot;
		Thread daily = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						finalBot.checkPtichka();
						Thread.sleep(1440 * 60 * 1000);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		};
		daily.setDaemon(true);
		daily.start();
		bot.foodParrot.start();
		OnceIn onceIn = new OnceIn(1, 2);
		while (true) {
			bot.checkLocation();
			bot.checkVoditchki();
			Thread.sleep(onceIn.nextLong());
		}
	}
}
