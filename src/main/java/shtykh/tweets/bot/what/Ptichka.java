package shtykh.tweets.bot.what;

import org.apache.log4j.Logger;
import shtykh.tweets.bot.Booleaner;

import static shtykh.tweets.bot.Bot.random;

/**
 * Created by shtykh on 29/03/15.
 */
public class Ptichka implements Stringer, Booleaner {
	private static Logger log = Logger.getLogger(Ptichka.class);
	private boolean newParty = true;
	private int number;
	private final Phrase black;
	private final Phrase white;

	public Ptichka() {
		this.number = 0;
		this.newParty = true;
		String[] begin = new String[] {"#птичка_певунья - ", "Сегодня #птичка_певунья - "};
		black = new Phrase("шоколадная. ", "коричневая. ");
		black.setCommentsBefore(begin);
		black.setCommentsAfter("Но день будет всё равно хорошим :)",
				":(",
				"¯ \\ _ (ツ) _ / ¯",
				"Не зря у меня болела голова!");
		white = new Phrase("ванильная. ", "белая. ");
		white.setCommentsBefore(begin);
		white.setCommentsAfter("И погода хорошая с утра :)",
				":)",
				"Так я и знала :)",
				"Всем доброго утра!");
	}
	
	public void add(int delta) {
		newParty = true;
		number += delta;
	}

	@Override
	public String nextString() {
		StringBuilder sb = new StringBuilder();
		if(newParty) {
			sb.append("Партия конфет #птичка_певунья подоспела :)");
			newParty = false;
			return sb.toString();
		}
		number--;
		if(number == 0) {
			sb.append("Последняя из этой партии #птичка_певунья - ");
		} else {
			if (random.nextBoolean()) {
				sb.append("Сегодня ");
			}
			sb.append("#птичка_певунья - ");
		}
		if (random.nextBoolean()) {
			sb.append(white.nextString());
		} else {
			sb.append(black.nextString());
		}
		return sb.toString();
	}

	public boolean isEmpty() {
		return number == 0;
	}

	@Override
	public boolean nextBoolean() {
		log.info("Ptichka-pevunia is empty :(");
		return isEmpty();
	}
}
