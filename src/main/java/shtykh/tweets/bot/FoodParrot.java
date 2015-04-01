package shtykh.tweets.bot;

import shtykh.tweets.TwitterClient;
import shtykh.tweets.bot.what.Phrase;
import shtykh.tweets.bot.when.OnceIn;

/**
 * Created by shtykh on 29/03/15.
 */
public class FoodParrot extends Parrot {
	private static Phrase food = new Phrase("мармеладками",
											"курицей",
											"едой",
											"мясом",
											"свёклой",
											"ватой",
											"мятой",
											"колбасой");
	static {
		food.setCommentsBefore("Обожралась ",
				"Ухомячилась ",
				"Объелась ",
				"Ужралась ",
				"Уелась ");
		food.setCommentsAfter(
				". Тошнит",
				". Плохо мне :(",
				" #ОфигевшийОпоссумОленька",
				"");
	}
	public FoodParrot(TwitterClient tc) {
		super(food, new OnceIn(1, 3), () -> Bot.random.nextInt() < 0.7, tc);
	}
}
