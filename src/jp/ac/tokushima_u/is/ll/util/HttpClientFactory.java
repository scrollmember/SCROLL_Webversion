package jp.ac.tokushima_u.is.ll.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class HttpClientFactory {
	public static DefaultHttpClient createHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		client.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(final HttpRequest request,
					final HttpContext context) throws HttpException,
					IOException {
				AuthState authState = (AuthState) context
						.getAttribute(ClientContext.TARGET_AUTH_STATE);
				CredentialsProvider credsProvider = (CredentialsProvider) context
						.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context
						.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

				if (authState.getAuthScheme() == null) {
					AuthScope authScope = new AuthScope(targetHost
							.getHostName(), targetHost.getPort());
					Credentials creds = credsProvider.getCredentials(authScope);
					if (creds != null) {
						authState.setAuthScheme(new BasicScheme());
						authState.setCredentials(creds);
					}
				}
			}
		}, 0);
		return client;
	}
	
	public static String convertStreamToString(InputStream is)throws UnsupportedEncodingException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
    
}
