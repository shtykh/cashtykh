package shtykh.task;

/**
 * Created by shtykh on 11/02/15.
 */
import javax.swing.*;
import java.awt.*;

public class TaskFrame<Result> extends JFrame implements Informable {

	private JLabel label;
	private JProgressBar progressBar;
	private JTextArea textArea;
	private Task<Result> task;
	private boolean visible;

	public TaskFrame(Task<Result> task, boolean visible) {
		this.task = task;
		this.visible = visible;
	}

	private void initComponents(){
		label = new JLabel("");
		add(label, BorderLayout.NORTH);

		textArea = new JTextArea(20, 30);
		add(new JScrollPane(textArea), BorderLayout.CENTER);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		add(progressBar, BorderLayout.SOUTH);

		task.addPropertyChangeListener(event -> {
			if ("progress".equals(event.getPropertyName())) {
				progressBar.setValue( (Integer)event.getNewValue() );
			}
		});
		task.execute();
	}

	public void start(){
		SwingUtilities.invokeLater(() -> {
			initComponents();
			pack();
			setVisible(visible);
			setAlwaysOnTop(true);
		});
	}

	@Override
	public void messageChanged(String message) {
		label.setText(message);
		textArea.append(message + "\n");
	}

	public void setResult(Result result) {
		String resultString = result == null ? "" : result.toString();
		messageChanged("Result: " + resultString);
		textArea.append("Done\n");
		progressBar.setVisible(false);
	}
}
