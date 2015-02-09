package shtykh;

import shtykh.storage.cache.TwoLevelCache;
import shtykh.ui.CacheTestDialogue;

/**
 * Created by shtykh on 06/02/15.
 */
public class Main {
	private static final int LEVEL_0_CAPACITY = 2;
	private static final int LEVEL_1_CAPACITY = 5;
	private static final boolean IS_LAST_ON_TOP = true;

	public static void main (String[] args) {
		TwoLevelCache cache = new TwoLevelCache<String, String>(LEVEL_0_CAPACITY, LEVEL_1_CAPACITY, IS_LAST_ON_TOP);
		CacheTestDialogue.show(cache);
		System.exit(0);
	}
}
