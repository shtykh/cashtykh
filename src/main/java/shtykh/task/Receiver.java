package shtykh.task;

/**
 * Created by shtykh on 11/02/15.
 */
public interface Receiver<Result> {
	public void onReceive(Result result);
}
