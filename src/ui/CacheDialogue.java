package ui;

import cashtykh.ICache;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;
import java.util.Iterator;

public class CacheDialogue extends JDialog {
    private JPanel contentPane;
    private JButton buttonAdd;
	private JButton buttonRemove;
	private JButton buttonGet;
    private JButton buttonCancel;
	private JList secondLevelList;
	private JList firstLevelList;
	private JSpinner spinner1;
	private JSpinner spinner2;
	ListModelCacheSync firstLevelListModel;
	ListModelCacheSync secondLevelListModel;
	private boolean isFirstListSelected;

	private int selectedIndex;
	private ICache cache;

	public CacheDialogue(ICache cache) {
		this.cache = cache;
		setContentPane(contentPane);
        setModal(true);

		firstLevelListModel = new ListModelCacheSync(cache, true);
		initList(firstLevelList,  firstLevelListModel);
		secondLevelListModel = new ListModelCacheSync(cache, false);
		initList(secondLevelList, secondLevelListModel);

		getRootPane().setDefaultButton(buttonAdd);

        buttonAdd.addActionListener(e -> onAdd());

		buttonGet.addActionListener(e -> onGet());

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

	private void onGet() {
		Object key;
		if (isFirstListSelected) {
			key = firstLevelListModel.get(selectedIndex);
		} else {
			key = secondLevelListModel.get(selectedIndex);
		}
		JOptionPane.showMessageDialog(null, cache.retrieve(key));
		secondLevelListModel.sync();
		firstLevelListModel.sync();
	}

	private void initList(JList jList, ListModelCacheSync listModel) {
		jList.addListSelectionListener(e -> onSelectionChanged(jList == firstLevelList, jList));
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setModel(listModel);
	}

	private void onSelectionChanged(boolean isFirstListSelected, JList list) {
		this.isFirstListSelected = isFirstListSelected;
		if (isFirstListSelected) {
			secondLevelList.clearSelection();
		} else {
			firstLevelList.clearSelection();
		}
		selectedIndex = list.getSelectedIndex();
	}

	private void onAdd() {
		String newString = StringInput.getString();
		firstLevelListModel.push(newString, newString);
		secondLevelListModel.sync();
    }

	private void onRemove() {
		if (isFirstListSelected) {
			firstLevelListModel.removeElement(selectedIndex);
			secondLevelListModel.sync();
		}
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
