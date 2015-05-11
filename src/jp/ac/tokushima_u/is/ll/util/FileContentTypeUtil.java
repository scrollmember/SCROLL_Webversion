package jp.ac.tokushima_u.is.ll.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileContentTypeUtil {
	public static String getMimeType(File file)
			throws java.io.IOException, MalformedURLException {
		String type = null;
		URL u = file.toURI().toURL();
		URLConnection uc = null;
		uc = u.openConnection();
		type = uc.getContentType();
		return type;
	}
}
