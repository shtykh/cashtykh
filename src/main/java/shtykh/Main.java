package shtykh;

import shtykh.storage.cache.IMultiLevelCache;
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
		IMultiLevelCache<String, Story> cache;
		try {
			cache = (IMultiLevelCache<String, Story>) new PoliticsInitializer().get();
			CacheTestDialogue.show(cache);
		} catch (IOException e) {
			UiUtil.showError("Initialization error", e, null);
		}
	}
}
