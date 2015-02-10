package shtykh.ui;

import oauth.signpost.exception.OAuthException;
import org.json.JSONException;
import shtykh.storage.cache.IMultiLevelCache;
import shtykh.storage.cache.TwoLevelCache;
import shtykh.util.Story;
import shtykh.tweets.TwitterClient;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import static shtykh.ui.UiUtil.showError;

public class CacheTestDialogue<Key> extends JDialog {
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
	private JButton buttonLoad;
	private JSlider tweetCountSlider;

	private boolean isFirstListSelected;
	private int selectedIndex;

	private IMultiLevelCache cache;
	private TwitterClient twitterClient;

	public CacheTestDialogue(IMultiLevelCache cache) {
		this.cache = cache;
		setContentPane(contentPane);
        setModal(true);
		setTitle("Cache test");

		initSpinner(spinner0, cache.getCapacityOfLevel(0), e -> onSpinnerChanged(spinner0, 0));
		initSpinner(spinner1, cache.getCapacityOfLevel(1), e -> onSpinnerChanged(spinner1, 1));

		initList(list0);
		initList(list1);

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
		buttonLoad.addActionListener(e -> onLoad());
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

	private void initList(JList jList) {
		jList.addListSelectionListener(e -> onSelectionChanged(jList == list0, jList));
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setModel(new DefaultListModel<Key>());
	}

	private void sync() {
		sync(cache, list0, 0);
		sync(cache, list1, 1);
	}

	private void sync(IMultiLevelCache cache, JList list, int level) {
		DefaultListModel listModel = (DefaultListModel) list.getModel();
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

	private void onLoad() {
		try {
			checkTwitterClient();
		} catch (JSONException | IOException e) {
			showError("Your authorization data is broken!", e);
			return;
		}
		String query = StringInput.getString();
		if (null != query) {
			try {
				Story tweets = twitterClient.searchTweets(query, tweetCountSlider.getValue());
				cache.put(query, tweets);
			} catch (OAuthException e) {
				showError("Authorization failed", e);
				return;
			} catch (IOException e) {
				showError("Adding to cache", e);
				return;
			} catch (JSONException e) {
				showError("Tweet parsing failed", e);
				e.printStackTrace();
			}
			sync();
		}
	}

	private void checkTwitterClient() throws JSONException, IOException {
		if (null == twitterClient) {
			twitterClient = new TwitterClient(this);
		}
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
		StoryInput.getStory((Story) gotFromCache);
		sync();
	}

	private Key getSelectedKey() {
		JList list = isFirstListSelected ? list0 : list1;
		ListModel<Key> listModel = list.getModel();
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
