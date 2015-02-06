import cashtykh.ICache;
import cashtykh.TwoLevelCache;
import ui.CacheDialogue;

/**
 * Created by shtykh on 06/02/15.
 */
public class Main {
	public static void main (String[] args) {
		ICache cache = new TwoLevelCache<String>(5, 10);
		CacheDialogue.show(cache);
		System.exit(0);
	}
}
