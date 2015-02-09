package ui;

import util.IdGenerator;
import util.Story;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StoryInput extends JDialog {
	private static IdGenerator idGenerator = new IdGenerator();

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField textField;
	private JTextPane textPane;

	private Story answer;

	public StoryInput(String answerByDefault, String story) {
		textField.setText(answerByDefault);
		textPane.setText(story);
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(e -> onOK());

		buttonCancel.addActionListener(e -> onCancel());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	private void onOK() {
		answer = new Story(textField.getText(), textPane.getText());
		dispose();
	}

	private void onCancel() {
		answer = null;
		dispose();
	}

	public static Story getStory() {
		StoryInput dialog = new StoryInput("Story_" + idGenerator.nextId(), Story.DEFAULT_STORY);
		dialog.pack();
		dialog.setVisible(true);
		return dialog.getAnswer();
	}

	private Story getAnswer() {
		return answer;
	}
}
