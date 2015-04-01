package shtykh.tweets.bot;

import org.junit.Test;
import shtykh.tweets.bot.what.Phrase;
import shtykh.tweets.bot.what.Ptichka;

public class PtichkaTest {

	private int sweetsNumber = 12;
	private Ptichka p = new Ptichka();
	
	@Test
	public void testNextString() throws Exception {
		p.add(sweetsNumber);
		for (int i = 0; i < sweetsNumber + 1; i++) {
			System.out.println(p.nextString());
		}
		Phrase food    = new Phrase(
				"мармеладками",
				"курицей",
				"едой",
				"мясом",
				"свёклой",
				"ватой",
				"мятой",
				"колбасой");
		food.setCommentsBefore("Обожралась ",
				"Ухомячилась ",
				"Объелась ",
				"Ужралась ");
		food.setCommentsAfter(". Тошнит", ". Плохо мне :(", " #ОфигевшийОпоссумОленька", "");
		for (int i = 0; i < sweetsNumber; i++) {
			System.out.println(food.nextString());
		}
	}
}