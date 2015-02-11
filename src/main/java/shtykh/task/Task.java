package shtykh.task;
import javax.swing.*;
import java.util.List;

/**
 * Created by shtykh on 11/02/15.
 */
public abstract class Task<Result> extends SwingWorker<Result, String> {
	
	private final TaskFrame taskFrame;
	private final Receiver<Result> receiver;
	private boolean disposeWhenDone;

	public Task(Receiver<Result> receiver) {
		this(receiver, true);
	}

	public Task(Receiver<Result> receiver, boolean disposeWhenDone){
		this.receiver = receiver;
		this.disposeWhenDone = disposeWhenDone;
		taskFrame = new TaskFrame(this);
	}

	@Override
	protected void done(){
		try {
			Result result = get();
			taskFrame.setResult(result);
			receiver.onReceive(result);
		}catch(Exception e){
			JOptionPane.showMessageDialog(taskFrame,
					e.getMessage(), this.getClass().getSimpleName(),
					JOptionPane.ERROR_MESSAGE);
		}
		if (disposeWhenDone) {
			taskFrame.dispose();
		}
	}

	@Override
	protected abstract Result doInBackground() throws Exception;

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
