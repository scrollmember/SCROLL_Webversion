package jp.ac.tokushima_u.is.ll.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

public class SerializeUtil {
	public static String serialize(Serializable obj) throws IOException {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		ObjectOutputStream objstream = new ObjectOutputStream(ostream);
		objstream.writeObject(obj);
		return new String(Base64.encodeBase64(ostream.toByteArray()));
	}

	public static Serializable deSerialize(String string) throws IOException,
			ClassNotFoundException {
		if (string == null || string.trim().equals(""))
			return new HashMap<String, Serializable>();
		byte[] bytes = Base64.decodeBase64(string.getBytes());
		ByteArrayInputStream istream = new ByteArrayInputStream(bytes);
		ObjectInputStream objstream = new ObjectInputStream(istream);
		return (Serializable) objstream.readObject();
	}
}
