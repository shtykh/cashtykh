package ui;

import cashtykh.ICache;
import com.sun.tools.javac.code.Scope;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CacheDialogue extends JDialog {
    private JPanel contentPane;
    private JButton buttonAdd;
	private JButton buttonRemove;
    private JButton buttonCancel;
	private JList list;
	ListModelCacheSync listModel;

	private int selectedIndex;

    public CacheDialogue(ICache cache) {
        setContentPane(contentPane);
        setModal(true);

		initList(cache);

		getRootPane().setDefaultButton(buttonAdd);

        buttonAdd.addActionListener(e -> onAdd());

		buttonRemove.addActionListener(e -> onRemove());

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

	private void initList(ICache cache) {
		listModel = new ListModelCacheSync(cache);
		list.addListSelectionListener(e -> onSelectionChanged(e));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(listModel);
	}

	private void onSelectionChanged(ListSelectionEvent e) {
		selectedIndex = list.getSelectedIndex();
	}

	private void onAdd() {
		String newString = StringInput.getString();
		listModel.push(newString, newString);
    }

	private void onRemove() {
		listModel.removeElement(selectedIndex);
		list.clearSelection();
	}

    private void onCancel() {
        dispose();
    }

    public static void show(ICache cache) {
        CacheDialogue dialog = new CacheDialogue(cache);
        dialog.pack();
        dialog.setVisible(true);
    }
}
