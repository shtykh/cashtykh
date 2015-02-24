package shtykh.util;

/**
 * Created by shtykh on 24/02/15.
 */
public interface Procedure<Content> {
	void apply(Content c);
}
