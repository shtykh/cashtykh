package shtykh;

import shtykh.storage.cache.IMultiLevelCache;
import shtykh.tweets.tag.Tag;
import shtykh.ui.CacheTestDialogue;
import shtykh.ui.UiUtil;
import shtykh.util.Story;
import shtykh.util.init.PoliticsInitializer;

import java.io.IOException;

/**
 * Created by shtykh on 06/02/15.
 */
public class Main {
	public static void main (String[] args) {
		IMultiLevelCache<Tag, Story> cache = null;
		try {
			cache = (IMultiLevelCache<Tag, Story>) new PoliticsInitializer().get();
		} catch (IOException e) {
			UiUtil.showError("Initialization error", e, null);
		}
		CacheTestDialogue.show(cache);
	}
}
