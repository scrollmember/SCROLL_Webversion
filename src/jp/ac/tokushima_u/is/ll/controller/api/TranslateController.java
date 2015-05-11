package jp.ac.tokushima_u.is.ll.controller.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.GoogleTranslateService;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/api/translate")
public class TranslateController {
	
    @Autowired
    private GoogleTranslateService googleTranslateService;
    @Autowired
    private UserService userService;
    
	@RequestMapping("/itemTitle")
	@ResponseBody
	public String itemTitle(String uid, String target, String titles){
		Users user = null;
		if(StringUtils.isBlank(uid)){
			user = SecurityUserHolder.getCurrentUser();
			user = userService.getById(user.getId());
			if(user==null){
				return "";
			}
		}else{
			user = userService.getById(uid);
			if(user==null) {
				return "";
			}
		}

		List<Language> langs = user.getMyLangs();
		langs.addAll(user.getStudyLangs());
		
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Map<String, String> titleMap = (Map<String, String>)gson.fromJson(titles, new TypeToken<Map<String, String>>(){}.getType());
		
		for(Language lang: langs){
			if(titleMap.containsKey(lang.getCode()) && !StringUtils.isBlank(titleMap.get(lang.getCode()))){
				return googleTranslateService.translateByCode(titleMap.get(lang.getCode()), lang.getCode(), target);
			}
		}
		
		for(String key: titleMap.keySet()){
			if(!StringUtils.isBlank(titleMap.get(key))){
				return googleTranslateService.translateByCode(titleMap.get(key), key, target);
			}
		}
		return "";
	}
	
	@RequestMapping("/text")
	@ResponseBody
	public String text(String uid, String src, String dest, String text){
		return googleTranslateService.translateByCode(text, src, dest);
	}
	
	@RequestMapping("/tts")
	public void tts(String text, String lang, HttpServletResponse res){
		if(StringUtils.isBlank(text)){
			return;
		}
		if(StringUtils.isBlank(lang)){
			lang = "en";
		}
		try {
			String url = "http://translate.google.com/translate_tts?ie=UTF-8&tl="+lang+"&q="+URLEncoder.encode(text.trim(), "UTF-8");
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){  
				HttpEntity entity = response.getEntity();
				if (entity != null) {  
					InputStream input = new BufferedInputStream(entity.getContent());
					res.setContentType("audio/mpeg");
					byte[] b = new byte[100];
			        int len;
		            while ((len = input.read(b)) > 0){
		                res.getOutputStream().write(b, 0, len);
		            }
		            input.close();
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
