package jp.ac.tokushima_u.is.ll.c2dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jp.ac.tokushima_u.is.ll.entity.C2DMConfig;
import jp.ac.tokushima_u.is.ll.service.C2DMService;
import jp.ac.tokushima_u.is.ll.util.HttpClientFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Stores config information related to Android Cloud to Device Messaging.
 * 
 */
class C2DMConfigLoader {
	String currentToken;

	private final C2DMService c2dmService;

	C2DMConfigLoader(C2DMService c2dmService) {
		this.c2dmService = c2dmService;
	}
	
	
	public static String getNewToken()throws Exception{
		DefaultHttpClient client = HttpClientFactory.createHttpClient();
		HttpPost httpPost = new HttpPost("https://www.google.com/accounts/ClientLogin");
		MultipartEntity params = new MultipartEntity();
		params.addPart("accountType", new StringBody("HOSTED_OR_GOOGLE"));
		params.addPart("Email", new StringBody("lemonrain99@gmail.com"));
		params.addPart("Passwd", new StringBody("limeng198"));
		params.addPart("service", new StringBody("ac2dm"));
		params.addPart("source", new StringBody("tokushima-learninglog-0"));
		httpPost.setEntity(params);
		HttpResponse response =	client.execute(httpPost);
		HttpEntity e = response.getEntity();
//		String content = HttpClientFactory.convertStreamToString(e.getContent());
		InputStream is = e.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
		String auth = null;
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				if(line.startsWith("Auth="))
					auth = line.substring("Auth=".length());
//				sb.append(line + "\n");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return auth;
	}

	/**
	 * Update the token.
	 * 
	 * Called on "Update-Client-Auth" or when admins set a new token.
	 * 
	 * @param token
	*/
	public void updateToken(String token) {
		if (token != null) {
			currentToken = token;
			C2DMConfig config = this.c2dmService.findC2DMConfig(C2DMConfig.Key);
			config.setAuthToken(token);
			this.c2dmService.saveC2DMConfig(config);
		}
	}
	
	
	public String updateToken() {
		try{
			currentToken = C2DMConfigLoader.getNewToken();
			C2DMConfig config = this.c2dmService.findC2DMConfig(C2DMConfig.Key);
			config.setAuthToken(currentToken);
			this.c2dmService.saveC2DMConfig(config);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return currentToken;
	}
	
	
	/**
	 * Token expired
	 */
	public void invalidateCachedToken() {
		currentToken = null;
	}

	/**
	 * Return the auth token.
	 * 
	 * It'll first memcache, if not found will use the database.
	 * 
	 * @return
	 */
	public String getToken() {
		if (currentToken == null) {
			currentToken = getC2DMConfig().getAuthToken();
		}
		return currentToken;
	}

	public C2DMConfig getC2DMConfig() {
		C2DMConfig dmConfig = c2dmService.findC2DMConfig(C2DMConfig.Key);
		if (dmConfig == null) {
			try{
				dmConfig = new C2DMConfig();
				dmConfig.setId(C2DMConfig.Key);
				dmConfig.setAuthToken(C2DMConfigLoader.getNewToken());
				c2dmService.saveC2DMConfig(dmConfig);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return dmConfig;
	}
	
	/*
	public C2DMConfig getC2DMConfig() {
		C2DMConfig dmConfig = c2dmService.findC2DMConfig(C2DMConfig.Key);
		// Create a new JDO object
		if (dmConfig == null) {
			dmConfig = new C2DMConfig();
			dmConfig.setId(C2DMConfig.Key);
			// Must be in classpath, before sending. Do not checkin !
			try {
				InputStream is = this.getClass().getClassLoader()
						.getResourceAsStream("/dataMessagingToken.txt");
				if (is != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is));
					String token = reader.readLine();
					dmConfig.setAuthToken(token);
				}
				c2dmService.saveC2DMConfig(dmConfig);
			} catch (Throwable t) {
				log.log(Level.SEVERE,
						"Can't load initial token, use admin console", t);
			}
		}
		return dmConfig;
	}
	*/
}
