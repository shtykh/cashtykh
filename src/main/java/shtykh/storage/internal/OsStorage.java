package shtykh.storage.internal;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import shtykh.storage.Storage;
import shtykh.util.Serializer;

/**
 * Created by shtykh on 07/02/15.
 */
public class OsStorage<Key, Value extends Serializable> implements Storage<Key, Value> {
	private static String directory = System.getProperty("user.dir") + "/cash_" + (new Date()).toString().replaceAll(":", "_") + "/";
	static {
		(new File(directory)).mkdirs();
	}

	private static <Key> String getFileName(Key key) {
		return directory + "/" + key;
	}

	@Override
	public Value get(Key key) throws IOException {
		Value value;
		try {
			value = (Value) Serializer.deserialize(getFileName(key));
		} catch (FileNotFoundException e) {
			return null;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Internal error while getting from OsStorage: \n" + e.getMessage());
		}
		return value;
	}

	@Override
	public Value put(Key key, Value value) throws IOException {
		Value deserialized = get(key);
		Serializer.serialize(getFileName(key), value);
		return deserialized;
	}

	@Override
	public Value remove(Key key) throws IOException {
		Value value = get(key);
		Files.deleteIfExists(getPath(key));
		return value;
	}

	@Override
	public void clear() throws IOException {
		FileUtils.deleteDirectory(new File(directory));
	}

	private Path getPath(Key key) {
		return FileSystems.getDefault().getPath(getFileName(key));
	}
}
