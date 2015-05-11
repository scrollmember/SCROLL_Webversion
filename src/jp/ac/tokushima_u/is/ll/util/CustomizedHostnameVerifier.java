package jp.ac.tokushima_u.is.ll.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class CustomizedHostnameVerifier implements HostnameVerifier {
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}
