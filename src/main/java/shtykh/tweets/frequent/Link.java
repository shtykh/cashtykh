package shtykh.tweets.frequent;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by shtykh on 24/02/15.
 */
public class Link extends FrequentText {
	private URI uri;

	private Link(URI uri, boolean add) {
		super(uri.toString(), add);
		this.uri = uri;
		
	}

	public static Link get(String text) throws URISyntaxException {
		URI uri = new URI(text);
		return new Link(uri, false);
	}

	public static Link create(String text) throws URISyntaxException {
		URI uri = new URI(text);
		return new Link(uri, true);
	}

	public URI getURI() {
		return uri;
	}
}
