package shtykh.util.init;

import shtykh.tweets.frequent.Tag;
import shtykh.util.Story;

import java.io.IOException;

/**
 * Created by shtykh on 17/02/15.
 */
public class PoliticsInitializer extends CacheInitializer<Tag, Story> {
	private static final String POLITICS_KEYWORDS = "#Путин! #Putin:\n" +
			"#Obama #Обама\n" +
			"#Крым #крымнаш #Донбасс #Украина'\n" +
			"#Санкции!!.. #Sanctions\n" +
			"#Merkel #Меркель$";

	public PoliticsInitializer() throws IOException {
		super(POLITICS_KEYWORDS, new TagFactory(), new StoryFactory());
	}
}
