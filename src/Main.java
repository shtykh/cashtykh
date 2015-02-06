import cashtykh.ICache;
import cashtykh.OneLevelCache;
import ui.CacheDialogue;

/**
 * Created by shtykh on 06/02/15.
 */
public class Main {
	public static void main (String[] args) {
		ICache cache = new OneLevelCache<String>();
		CacheDialogue.show(cache);
		System.exit(0);
	}
}
