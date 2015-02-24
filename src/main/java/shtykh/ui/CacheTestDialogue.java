package shtykh.ui;

import org.json.JSONException;
import shtykh.storage.cache.ICache;
import shtykh.storage.cache.IMultiLevelCache;
import shtykh.task.Receiver;
import shtykh.tweets.Discover;
import shtykh.tweets.SearchTweets;
import shtykh.tweets.Tweets;
import shtykh.tweets.TwitterClient;
import shtykh.tweets.tag.Tag;
import shtykh.util.Story;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static shtykh.tweets.StoryEngine.getAllFrequentLinks;
import static shtykh.ui.UiUtil.showError;

public class CacheTestDialogue extends JFrame implements Receiver<Tweets> {
    private JPanel contentPane;
    private JButton buttonAdd;
	private JButton buttonRemove;
	private JButton buttonEdit;
    private JButton buttonDiscover;
	private JButton buttonLoad;
	private JButton buttonBrowse;
	private JList list0;
	private JList list1;
	private JSpinner spinner0;
	private JSpinner spinner1;
	private JCheckBox lastOnTop;
	private JSlider tweetCountSlider;
	private JSlider iterationNumberSlider;
	private JLabel size0;
	private JLabel size1;

	private boolean isFirstListSelected;
	private int selectedIndex;

	private IMultiLevelCache<Tag, Story> cache;
	private TwitterClient twitterClient;

	public CacheTestDialogue(IMultiLevelCache<Tag, Story> cache) {
		this.cache = cache;
		setContentPane(contentPane);

		setTitle("Story viewer");

		initSpinner(spinner0, cache.getCapacityOfLevel(0), e -> onSpinnerChanged(spinner0, 0));
		initSpinner(spinner1, cache.getCapacityOfLevel(1), e -> onSpinnerChanged(spinner1, 1));

		initList(list0, spinner0);
		initList(list1, spinner1);

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
		sync();
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

	public static void show(IMultiLevelCache<Tag, Story> cache) {
		CacheTestDialogue dialog = new CacheTestDialogue(cache);
		dialog.pack();
		dialog.setVisible(true);
	}

	private void initButtons() {
		getRootPane().setDefaultButton(buttonAdd);

		buttonAdd.addActionListener(e -> onAdd());
		buttonLoad.addActionListener(e -> onLoad());
		buttonEdit.addActionListener(e -> onEdit());
		buttonRemove.addActionListener(e -> onRemove());
		buttonDiscover.addActionListener(e -> onDiscover());
		buttonBrowse.addActionListener(e -> onBrowse());
	}

	private void initSpinner(JSpinner spinner, int capacity, ChangeListener changeListener) {
		spinner.setValue(capacity);
		JComponent comp = spinner.getEditor();
		JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
		DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
		formatter.setCommitsOnValidEdit(true);
		spinner.addChangeListener(changeListener);
	}

	private void initList(JList jList, JSpinner capacitySpinner) {
		jList.setCellRenderer(new MyListCellRenderer(capacitySpinner));
		jList.addListSelectionListener(e -> onSelectionChanged(jList == list0, jList));
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setModel(new DefaultListModel<Tag>());
	}

	private void sync() {
		sync(cache, list0, size0, 0);
		sync(cache, list1, size1, 1);
	}

	private void sync(IMultiLevelCache<Tag, Story> cache, JList<Tag> list, JLabel sizeLabel, int level) {
		DefaultListModel<Tag> listModel = (DefaultListModel) list.getModel();
		listModel.clear();
		Iterator<Tag> keyIterator = cache.keyIteratorOfLevel(level);
		keyIterator.forEachRemaining(obj -> listModel.addElement(obj));
		sizeLabel.setText(String.valueOf(cache.getSizeOfLevel(level)));
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
			showError("Your authorization data is broken!", e, this);
			return;
		}
		String query = StringInput.getString();
		if (null != query) {
			new SearchTweets(twitterClient, this, Tag.get(query), tweetCountSlider.getValue()).start();
		}
	}

	private void onBrowse() {
		List<URI> uris = new ArrayList<>();
		for (String link : getAllFrequentLinks()) {
			try {
				URI uri = new URI(link);
				uris.add(uri);
			} catch (URISyntaxException e) {
			}
		}
		ListDialog.create(uris, uri ->{
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				UiUtil.showError("link has failed to open", e, this);
			}
		}, "Choose the link to browse it");
	}

	private void checkTwitterClient() throws JSONException, IOException {
		if (null == twitterClient) {
			twitterClient = new TwitterClient(this);
		}
	}

	private void onDiscover() {
		try {
			checkTwitterClient();
		} catch (JSONException | IOException e) {
			showError("Your authorization data is broken!", e, this);
			return;
		}
		new Discover(twitterClient, this, (ICache<Tag, Story>) cache, 40, tweetCountSlider.getValue(), iterationNumberSlider.getValue()).start();
		sync();
	}

	private void onAdd() {
		Story newStory = StoryInput.getStory();
		add(newStory);
	}

	private void add(Story newStory) {
		if (null != newStory) {
			try {
				Tag tag = Tag.get(newStory.getTitle());
				cache.put(tag, newStory);
			} catch (IOException e) {
				showError("Adding to cache", e, this);
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
			showError("Removing from cache", e, this);
			return;
		}
		sync();
	}

	private void onEdit() {
		Serializable gotFromCache;
		try {
			gotFromCache = cache.remove(getSelectedKey());
		} catch (IOException e) {
			showError("Getting from cache", e, this);
			return;
		}
		Story storyEdited = StoryInput.getStory((Story) gotFromCache);
		add(storyEdited);
	}

	private Tag getSelectedKey() {
		JList list = isFirstListSelected ? list0 : list1;
		ListModel<Tag> listModel = list.getModel();
		return selectedIndex >= 0 ? listModel.getElementAt(selectedIndex) : null;
	}

	private void onCancel() {
		try {
			cache.clear();
		} catch (IOException e) {
			showError("Clearing cache", e, this);
		}
		dispose();
		System.exit(0);
    }

	@Override
	public void onReceive(Tweets tweets) {
		add(tweets);
	}

	private static class MyListCellRenderer implements ListCellRenderer{

		private final JLabel jlblCell = new JLabel(" ", JLabel.LEFT);
		private final JSpinner capacitySpinner;
		Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
		Border emptyBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.GRAY);
		Border lastSelectedBorder = BorderFactory.createMatteBorder(2, 2, 5, 2, Color.BLACK);
		Border lastEmptyBorder = BorderFactory.createMatteBorder(0, 1, 5, 1, Color.GRAY);

		public MyListCellRenderer(JSpinner capacitySpinner) {
			super();
			this.capacitySpinner = capacitySpinner;
		}

		@Override
		public Component getListCellRendererComponent(JList jList, Object value,
													  int index, boolean isSelected, boolean cellHasFocus) {

			jlblCell.setOpaque(true);
			jlblCell.setText(value.toString());
			if (isSelected) {
				jlblCell.setForeground(Color.WHITE);
				jlblCell.setBackground(jList.getSelectionBackground());
			} else {
				jlblCell.setForeground(Color.BLACK);
				jlblCell.setBackground(jList.getBackground());
			}
			boolean isLast = index == ((int)capacitySpinner.getValue()) - 1;
			Border border = cellHasFocus ? 
					(isLast ? lastSelectedBorder : selectedBorder) : 
					(isLast ? lastEmptyBorder : emptyBorder);
			jlblCell.setBorder(border);

			return jlblCell;
		}
	}
}
