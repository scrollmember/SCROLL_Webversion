package jp.ac.tokushima_u.is.ll.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
	
	public static String getHostname(){
		try {
			return InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "localhost";
		}
	}
}
