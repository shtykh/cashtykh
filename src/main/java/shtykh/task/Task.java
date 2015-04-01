package shtykh.task;
import shtykh.ui.UiUtil;

import javax.swing.*;
import java.util.List;

/**
 * Created by shtykh on 11/02/15.
 */
public abstract class Task<Result> extends SwingWorker<Result, String> {
	
	private final TaskFrame<Result> taskFrame;
	private final Receiver<Result> receiver;
	private boolean disposeWhenDone;

	public Task(Receiver<Result> receiver) {
		this(receiver, true, true);
	}

	public Task(Receiver<Result> receiver, boolean disposeWhenDone, boolean visible){
		this.receiver = receiver;
		this.disposeWhenDone = disposeWhenDone;
		taskFrame = new TaskFrame(this, visible);
	}

	@Override
	protected void done(){
		try {
			Result result = get();
			taskFrame.setResult(result);
			receiver.onReceive(result);
		}catch(Exception e){
			UiUtil.showError(this.getClass().getSimpleName(), e, taskFrame);
		} finally {
			if (disposeWhenDone) {
				taskFrame.dispose();
			}
		}
	}

	@Override
	protected abstract Result doInBackground() throws Exception;

	public Receiver<Result> getReceiver() {
		return receiver;
	}

	@Override
	protected void process(List<String> chunks){
		for(String message : chunks){
			taskFrame.messageChanged(message);
		}
	}
	
	public void start() {
		taskFrame.start();
	}
}
