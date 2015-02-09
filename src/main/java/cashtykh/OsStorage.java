package cashtykh;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Created by shtykh on 07/02/15.
 */
public class OsStorage<Key, Value extends Serializable> implements Storage<Key, Value> {
	private static String currentDirectory = System.getProperty("user.dir") + "/cashed/" + (new Date()).toString() + "/";
	static {
		(new File(currentDirectory)).mkdirs();
	}

	private static <Key> String getFileName(Key key) {
		return currentDirectory + "/" + key;
	}

	private static void serialize(String fileName, Object object) {
		try(FileOutputStream fos = new FileOutputStream(fileName)) {
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Object deserialize(String fileName) throws FileNotFoundException{
		Object obj = null;
		try(FileInputStream fis = new FileInputStream(fileName)) {
			ObjectInputStream oin = new ObjectInputStream(fis);
			obj = oin.readObject();
			oin.close();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public Value get(Key key) {
		Value value;
		try {
			value = (Value) deserialize(getFileName(key));
		} catch (FileNotFoundException e) {
			return null;
		}
		return value;
	}

	@Override
	public Value put(Key key, Value value) {
		Value deserialized = get(key);
		serialize(getFileName(key), value);
		return deserialized;
	}

	@Override
	public Value remove(Key key) {
		Value value = get(key);
		try {
			Files.deleteIfExists(getPath(key));
		} catch (IOException e) {
			throw new RuntimeException("Removing " + key + " : error!");
		}
		return value;
	}

	private Path getPath(Key key) {
		return FileSystems.getDefault().getPath(getFileName(key));
	}
}
