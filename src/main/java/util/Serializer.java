package util;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shtykh on 06/02/15.
 */
public class Serializer {
	private static String currentDirectory = System.getProperty("user.dir") + "/cashed/" + (new Date()).toString() + "/";
	static {
		(new File(currentDirectory)).mkdirs();
	}

	public static <Key> String getFileName(Key key) {
		return currentDirectory + "/" + key;
	}

	public static void serialize(String fileName, Object object) {
		try(FileOutputStream fos = new FileOutputStream(fileName)) {
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object deserialize(String fileName) {
		Object obj = null;
		try(FileInputStream fis = new FileInputStream(fileName)) {
			ObjectInputStream oin = new ObjectInputStream(fis);
			obj = oin.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
