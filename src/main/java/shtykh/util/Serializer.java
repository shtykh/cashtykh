package shtykh.util;

import java.io.*;
import java.util.Date;

/**
 * Created by shtykh on 06/02/15.
 */
public class Serializer {

	public static void serialize(String fileName, Object object) throws IOException {
		try(FileOutputStream fos = new FileOutputStream(fileName)) {
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.flush();
			oos.close();
		}
	}

	public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
		Object obj;
		try(FileInputStream fis = new FileInputStream(fileName)) {
			ObjectInputStream oin = new ObjectInputStream(fis);
			obj = oin.readObject();
			oin.close();
		}
		return obj;
	}
}
