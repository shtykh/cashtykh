package shtykh.ui;

import shtykh.util.Procedure;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.List;

public class ListDialog<Content> extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JList list;
	private Content chosen;
	private final Procedure<Content> applyToChosen;

	private ListDialog(Collection<Content> args, Procedure<Content> applyToChosen, String title) {
		this.applyToChosen = applyToChosen;
		setContentPane(contentPane);
		setModal(true);
		setTitle(title);
		getRootPane().setDefaultButton(buttonOK);
		
		DefaultListModel<Content> model = new DefaultListModel<>();
		for (Content arg : args) {
			model.addElement(arg);
		}
		list.setModel(model);
		
		list.addListSelectionListener(e -> onSelectionChanged(e));

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

	private void onSelectionChanged(ListSelectionEvent e) {
		int firstIndex = e.getFirstIndex();
		if (firstIndex >= 0 && firstIndex < list.getModel().getSize()) {
			chosen = (Content) list.getModel().getElementAt(firstIndex);
		}
	}

	private void onOK() {
		if (chosen != null) {
			applyToChosen.apply(chosen);
		}
		dispose();
	}

	private void onCancel() {
		dispose();
	}

	public static <Content> void create(List<Content> args, Procedure<Content> applyToChosen, String title) {
		ListDialog<Content> dialog = new ListDialog<>(args, applyToChosen, title);
		dialog.pack();
		dialog.setVisible(true);
	}
}
