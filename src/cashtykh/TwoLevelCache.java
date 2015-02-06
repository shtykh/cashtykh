package cashtykh;

import util.Serializer;

import java.util.*;

/**
 * Created by shtykh on 06/02/15.
 */
public class TwoLevelCache<Value> extends LinkedHashMap<String, Value> implements ICache<String, Value> {

	private final int firstLevelCapacity;
	private int secondLevelCapacity;

	private LinkedList<String> fileNames;

	public TwoLevelCache(int firstLevelCapacity, int secondLevelCapacity) {
		super(16, 0.75f, true);
		this.firstLevelCapacity = firstLevelCapacity;
		this.secondLevelCapacity = secondLevelCapacity;
		fileNames = new LinkedList<>();
	}

	@Override
	public Value retrieve(String key) throws NoSuchElementException {
		return get(key);
	}

	@Override
	public void cache(String key, Value value) {
		put(key, value);
	}

	@Override
	public boolean delete(String key) {
		boolean success = null != remove(key);
		deserializeIfNeeded();
		return success;
	}

	private void deserializeIfNeeded() {
		if (super.size() < firstLevelCapacity && !fileNames.isEmpty()) {
			String key = fileNames.pollFirst();
			String fileName = Serializer.getFileName(key);
			Value deserialized = (Value) Serializer.deserialize(fileName);
			put(key, deserialized);
		}
	}

	@Override
	public Iterator<String> firstLevelIterator() {
		return keySet().iterator();
	}

	@Override
	public Iterator<String> secondLevelIterator() {
		return fileNames.iterator();
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<String, Value> eldest) {
		if (super.size() >= firstLevelCapacity) {
			fileNames.offerFirst(eldest.getKey());
			Serializer.serialize(Serializer.getFileName(eldest.getKey()), eldest.getValue());
			remove(eldest.getKey());
		}
		return false;
	}

	@Override
	public int size() {
		return super.size() + fileNames.size();
	}
}
