package shtykh.task;
import javax.swing.*;
import java.util.List;

/**
 * Created by shtykh on 11/02/15.
 */
public abstract class Task<Result> extends SwingWorker<Result, String> {
	
	private final TaskFrame taskFrame;
	private final Receiver<Result> receiver;

	public Task(Receiver<Result> receiver){
		this.receiver = receiver;
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
					"Error", "Search",
					JOptionPane.ERROR_MESSAGE);
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
