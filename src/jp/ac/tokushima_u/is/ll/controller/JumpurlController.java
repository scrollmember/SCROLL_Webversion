package jp.ac.tokushima_u.is.ll.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.apache.activemq.security.SecurityContext;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//
//@Controller
//@RequestMapping("/jumpurl")
//public class JumpurlController {
//	
//	@Autowired
//	private UserService userService;
//	
//	@RequestMapping(method = RequestMethod.GET)
//	public String redirect(String url, String key, String email){
//		if(StringUtils.isBlank(url) || StringUtils.isBlank(key) || StringUtils.isBlank(email)){
//			return "redirect:/";
//		}
//		Users user = (Users)userService.findByEmail(email);
//		if(user == null){
//			return "redirect:/";
//		}
//		
//		if(SecurityUtils.getSubject().isAuthenticated()){
//			return "redirect:" + URLDecoder.decode(url, "UTF-8");
//		}else{
//			String testkey = Hex.encodeHexString(Digests.sha1(url.getBytes(), (user.getCreateTime().toString()+ user.getId()+"fa133562-7037-40e4-a93d-0751cefabf1c").getBytes()));
//			if(key.equals(testkey)){
//				//TODO
//				SecurityUtils.getSubject().login(arg0);
//			}
//		}
//		
//		String testkey = Hex.encodeHexString(Digests.sha1(url.getBytes(), (user.getCreateTime().toString()+ user.getId()+"fa133562-7037-40e4-a93d-0751cefabf1c").getBytes()));
//		if(key.equals(testkey)){
//			if(SecurityUserHolder.getCurrentUser()==null){
//				RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken("fa133562-7037-40e4-a93d-0751cefabf1c", user, user.getAuthorities()); 
//				auth.setAuthenticated(true); 
//				SecurityContext c = SecurityContextHolder.getContext(); 
//				if (c instanceof SecurityContext) ((SecurityContext)c).setAuthentication(auth); 
//			}
//			try {
//				return "redirect:" + URLDecoder.decode(url, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				return "redirect:/";
//			}
//		}else{
//			return "redirect:/";
//		}
//	}
//	
//	public static void main(String[] args) throws UnsupportedEncodingException{
//		String url = URLEncoder.encode("http://localhost:8080/learninglog/item/402880d62a9ee0dd012a9ee13b6c0001", "UTF-8");
//		System.out.println(url);
//		System.out.println(URLDecoder.decode(url, "UTF-8"));
//	}
//}
