package ui;

import cashtykh.ICache;
import cashtykh.TwoLevelCache;
import util.Story;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.event.*;
import java.io.Serializable;

public class CacheTestDialogue<Key extends Serializable> extends JDialog {
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

	public CacheTestDialogue(TwoLevelCache cache) {
		this.cache = cache;
		setContentPane(contentPane);
        setModal(true);

		initSpinner(spinner1, cache.getCapacity1(), e -> {
			cache.setCapacity1((int) spinner1.getValue());
			firstLevelListModel.sync();
			secondLevelListModel.sync();
		});
		initSpinner(spinner2, cache.getCapacity2(), e -> {
			cache.setCapacity2((int) spinner2.getValue());
			firstLevelListModel.sync();
			secondLevelListModel.sync();
		});

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

	private void initSpinner(JSpinner spinner, int capacity, ChangeListener changeListener) {
		spinner.setValue(capacity);
		JComponent comp = spinner.getEditor();
		JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
		DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
		formatter.setCommitsOnValidEdit(true);
		spinner.addChangeListener(changeListener);
	}

	private void onGet() {
		Key key;
		if (isFirstListSelected) {
			key = (Key) firstLevelListModel.get(selectedIndex);
		} else {
			key = (Key) secondLevelListModel.get(selectedIndex);
		}
		JOptionPane.showMessageDialog(null, cache.get(key));
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
		if (null != newString) {
			firstLevelListModel.push(newString, new Story(newString));
			secondLevelListModel.sync();
		}
    }

	private void onRemove() {
		if (isFirstListSelected) {
			firstLevelListModel.removeElement(selectedIndex);
			secondLevelListModel.sync();
		} else {
			firstLevelListModel.sync();
			secondLevelListModel.removeElement(selectedIndex);
		}
	}

    private void onCancel() {
        dispose();
    }

    public static void show(TwoLevelCache cache) {
        CacheTestDialogue dialog = new CacheTestDialogue(cache);
        dialog.pack();
        dialog.setVisible(true);
    }
}
