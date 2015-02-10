package shtykh.ui;

import shtykh.util.IdGenerator;

import javax.swing.*;
import java.awt.event.*;

public class StringInput extends JDialog {
	private static IdGenerator idGenerator = new IdGenerator("String");

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField textField;

	private String answer;

	public StringInput(String answerByDefault) {
		textField.setText(answerByDefault);
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
		answer = textField.getText();
		dispose();
	}

	private void onCancel() {
		answer = "";
		dispose();
	}

	public static String getString() {
		StringInput dialog = new StringInput(idGenerator.nextName());
		dialog.pack();
		dialog.setVisible(true);
		return dialog.getAnswer();
	}

	private String getAnswer() {
		return answer;
	}
}
