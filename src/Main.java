import cashtykh.TwoLevelCache;
import ui.CacheTestDialogue;

/**
 * Created by shtykh on 06/02/15.
 */
public class Main {
	public static void main (String[] args) {
		TwoLevelCache cache = new TwoLevelCache<String, String>(3, 2);
		CacheTestDialogue.show(cache);
		System.exit(0);
	}
}
