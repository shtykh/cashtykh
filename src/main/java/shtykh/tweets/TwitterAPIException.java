package shtykh.tweets;

/**
 * Created by shtykh on 17/02/15.
 */
public class TwitterAPIException extends Exception {
	public TwitterAPIException(String message) {
		super(message);
	}
	public TwitterAPIException(Exception cause) {
		super(cause);
	}
}
