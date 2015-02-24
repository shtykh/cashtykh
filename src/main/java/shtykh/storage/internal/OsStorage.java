package shtykh.storage.internal;

import org.apache.commons.io.FileUtils;
import shtykh.storage.Storage;
import shtykh.util.Serializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 * Created by shtykh on 07/02/15.
 */
public class OsStorage<Key, Value extends Serializable> implements Storage<Key, Value> {
	private static String directory = System.getProperty("user.dir") + "/cash_" + (new Date()).toString().replaceAll(":", "_");
	
	private void ensureDirectoryExists(){
		File file = new File(directory);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
	}

	private static <Key> String getFileName(Key key) {
		if (key.toString().length() == 0) {
			throw new IllegalArgumentException("key.toString() could not be empty!");
		}
		return directory + "/" + key;
	}

	@Override
	public Value get(Key key) throws IOException {
		ensureDirectoryExists();
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
		ensureDirectoryExists();
		Value deserialized = remove(key);
		Serializer.serialize(getFileName(key), value);
		return deserialized;
	}

	@Override
	public Value remove(Key key) throws IOException {
		ensureDirectoryExists();
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
