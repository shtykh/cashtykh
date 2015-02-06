package cashtykh;

import util.Serializer;

import java.util.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class TwoLevelCache<Key, Value> implements ICache<Key, Value> {

	private final int firstLevelCapacity;
	private int secondLevelCapacity;

	private Map<Key, Value> firstLevel;

	private LinkedList<Key> firstLevelKeys;
	private LinkedList<Key> secondLevelKeys;

	public TwoLevelCache(int firstLevelCapacity, int secondLevelCapacity) {
		this.firstLevelCapacity = firstLevelCapacity;
		this.secondLevelCapacity = secondLevelCapacity;
		firstLevel = new HashMap<>();
		firstLevelKeys = new LinkedList<>();
		secondLevelKeys = new LinkedList<>();
	}

	@Override
	public Value retrieve(Key key) throws NoSuchElementException {
		Value value = delete(key);
		cache(key, value);
		return value;
	}

	@Override
	public void cache(Key key, Value value) {
		firstLevelKeys.offerFirst(key);
		firstLevel.put(key, value);
		serializeIfNeeded();
	}

	@Override
	public Value delete(Key key) {
		if (firstLevelKeys.contains(key)) {
			firstLevelKeys.remove(key);
			Value deleted = firstLevel.remove(key);
			deserializeIfNeeded();
			return deleted;
		} else {
			// TODO remove file
			String fileName = Serializer.getFileName(key);
			Value deserialized = (Value) Serializer.deserialize(fileName);
			secondLevelKeys.remove(key);
			return deserialized;
		}
	}

	private void serializeIfNeeded() {
		while (firstLevelKeys.size() > firstLevelCapacity) {
			Key eldestKey = firstLevelKeys.pollLast();
			secondLevelKeys.offerFirst(eldestKey);
			while (secondLevelKeys.size() > secondLevelCapacity) {
				secondLevelKeys.removeLast();
				//todo remove file
			}
			Serializer.serialize(Serializer.getFileName(eldestKey), firstLevel.get(eldestKey));
			firstLevel.remove(eldestKey);
		}
	}

	private void deserializeIfNeeded() {
		while (firstLevelKeys.size() < firstLevelCapacity && !secondLevelKeys.isEmpty()) {
			Key key = secondLevelKeys.pollFirst();
			String fileName = Serializer.getFileName(key);
			Value deserialized = (Value) Serializer.deserialize(fileName);
			firstLevel.put(key, deserialized);
		}
	}

	@Override
	public Iterator<Key> firstLevelIterator() {
		return firstLevelKeys.iterator();
	}

	@Override
	public Iterator<Key> secondLevelIterator() {
		return secondLevelKeys.iterator();
	}

	@Override
	public int size() {
		return firstLevelKeys.size() + secondLevelKeys.size();
	}
}
