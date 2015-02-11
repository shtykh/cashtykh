package shtykh.ui;

import shtykh.util.IdGenerator;
import shtykh.util.Story;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static shtykh.ui.UiUtil.showError;

public class StoryInput extends JDialog {
	private static IdGenerator idGenerator = new IdGenerator("Story");
	private static String DEFAULT_STORY = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " + "\n" +
			"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " + "\n" +
			"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " + "\n" +
			"ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " + "\n" +
			"in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " + "\n" +
			"Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " + "\n" +
			"officia deserunt mollit anim id est laborum.";

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

		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	public static Story getStory() {
		StoryInput dialog = new StoryInput(idGenerator.nextName(), DEFAULT_STORY);
		dialog.pack();
		dialog.setVisible(true);
		return dialog.getAnswer();
	}

	public static Story getStory(Story gotFromCache) {
		StoryInput dialog = new StoryInput(gotFromCache.getTitle(), gotFromCache.toString());
		dialog.pack();
		dialog.setVisible(true);
		return dialog.getAnswer();
	}

	private void onOK() {
		answer = new Story(textField.getText(), textPane.getText());
		try {
			checkAnswer(answer);
		} catch (Exception e) {
			showError("Story is not OK!", e, this);
			return;
		}
		dispose();

	}

	private void checkAnswer(Story story) throws Exception{
		if (story == null) {
			throw new NullPointerException("Story shouldn't be null");

		} else if (story.getTitle() == null) {
			throw new NullPointerException("Story's title shouldn't be null");

		}
		if (story.getTitle().length() == 0) {
			throw new Exception("Story's title shouldn't be empty");
		}
	}

	private void onCancel() {
		answer = null;
		dispose();
	}

	private Story getAnswer() {
		return answer;
	}
}
