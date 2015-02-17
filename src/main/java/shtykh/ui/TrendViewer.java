package shtykh.ui;

import shtykh.storage.keys.Context;
import shtykh.storage.keys.Tag;

import javax.swing.*;
import java.awt.event.*;

public class TrendViewer extends JFrame {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JList list1;

	public TrendViewer() {
		DefaultListModel<Tag> listModel = new DefaultListModel<>();
		for (Tag tag : Context.getInstance().getTagList()) {
			listModel.addElement(tag);
		}
		list1.setModel(listModel);
		setContentPane(contentPane);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(e -> onOK());

		buttonCancel.addActionListener(e -> onCancel());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(
				e -> onCancel(), 
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), 
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	private void onOK() {
		dispose();
	}

	private void onCancel() {
		dispose();
	}

	public static void display() {
		TrendViewer dialog = new TrendViewer();
		dialog.pack();
		dialog.setVisible(true);
	}
}
