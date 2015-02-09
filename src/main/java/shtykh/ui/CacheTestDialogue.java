package shtykh.ui;

import shtykh.storage.cache.IMultiLevelCache;
import shtykh.storage.cache.TwoLevelCache;
import shtykh.util.Story;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import static shtykh.ui.UiUtil.showError;

public class CacheTestDialogue<Key extends Serializable> extends JDialog {
    private JPanel contentPane;
    private JButton buttonAdd;
	private JButton buttonRemove;
	private JButton buttonGet;
    private JButton buttonCancel;
	private JList list0;
	private JList list1;
	private JSpinner spinner0;
	private JSpinner spinner1;
	private JCheckBox lastOnTop;

	DefaultListModel listModel0;
	DefaultListModel listModel1;

	private boolean isFirstListSelected;
	private int selectedIndex;

	private IMultiLevelCache cache;

	public CacheTestDialogue(IMultiLevelCache cache) {
		this.cache = cache;
		setContentPane(contentPane);
        setModal(true);
		setTitle("Cache test");

		initSpinner(spinner0, cache.getCapacityOfLevel(0), e -> onSpinnerChanged(spinner0, 0));
		initSpinner(spinner1, cache.getCapacityOfLevel(1), e -> onSpinnerChanged(spinner1, 1));

		listModel0 = new DefaultListModel();
		initList(list0, listModel0);
		listModel1 = new DefaultListModel();
		initList(list1, listModel1);

		initButtons();

		lastOnTop.setSelected(cache.isLastOnTop());
		lastOnTop.addActionListener(e -> onLastSeenOnTopChanged());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

	private void onSpinnerChanged(JSpinner spinner, int level) {
		int newCapacity = (int) spinner.getValue();
		if (newCapacity >= 0) {
			cache.setCapacityOfLevel(level, newCapacity);
			sync();
		} else {
			spinner.setValue(0);
		}
	}

	public static void show(TwoLevelCache cache) {
		CacheTestDialogue dialog = new CacheTestDialogue(cache);
		dialog.pack();
		dialog.setVisible(true);
	}

	private void initButtons() {
		getRootPane().setDefaultButton(buttonAdd);

		buttonAdd.addActionListener(e -> onAdd());
		buttonGet.addActionListener(e -> onGet());
		buttonRemove.addActionListener(e -> onRemove());
		buttonCancel.addActionListener(e -> onCancel());
	}

	private void initSpinner(JSpinner spinner, int capacity, ChangeListener changeListener) {
		spinner.setValue(capacity);
		JComponent comp = spinner.getEditor();
		JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
		DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
		formatter.setCommitsOnValidEdit(true);
		spinner.addChangeListener(changeListener);
	}

	private void initList(JList jList, DefaultListModel listModel) {
		jList.addListSelectionListener(e -> onSelectionChanged(jList == list0, jList));
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setModel(listModel);
	}

	private void sync() {
		sync(cache, listModel0, 0);
		sync(cache, listModel1, 1);
	}

	private void sync(IMultiLevelCache cache, DefaultListModel listModel, int level) {
		listModel.clear();
		Iterator keyIterator = cache.keyIteratorOfLevel(level);
		keyIterator.forEachRemaining(obj -> listModel.addElement(obj));
	}

	private void onLastSeenOnTopChanged() {
		cache.setLastOnTop(lastOnTop.isSelected());
	}

	private void onSelectionChanged(boolean isFirstListSelected, JList list) {
		this.isFirstListSelected = isFirstListSelected;
		if (isFirstListSelected) {
			list1.clearSelection();
		} else {
			list0.clearSelection();
		}
		selectedIndex = list.getSelectedIndex();
	}

	private void onAdd() {
		Story newStory = StoryInput.getStory();
		if (null != newStory) {
			try {
				cache.put(newStory.getTitle(), newStory);
			} catch (IOException e) {
				showError("Adding to cache", e);
				return;
			}
			sync();
		}
    }

	private void onRemove() {
		if (selectedIndex < 0) {
			return;
		}
		try {
			cache.remove(getSelectedKey());
		} catch (IOException e) {
			showError("Removing from cache", e);
			return;
		}
		sync();
	}

	private void onGet() {
		Serializable gotFromCache = null;
		try {
			gotFromCache = cache.get(getSelectedKey());
		} catch (IOException e) {
			showError("Getting from cache", e);
			return;
		}
		JOptionPane.showMessageDialog(null, gotFromCache, "", JOptionPane.PLAIN_MESSAGE);
		sync();
	}

	private Key getSelectedKey() {
		ListModel<Key> listModel = isFirstListSelected ? listModel0 : listModel1;
		return listModel.getElementAt(selectedIndex);
	}

	private void onCancel() {
		try {
			cache.clear();
		} catch (IOException e) {
			showError("Clearing cache", e);
		}
		dispose();
    }
}
