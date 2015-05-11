package jp.ac.tokushima_u.is.ll.controller;
//TODO Delete
//
//import java.io.IOException;
//import java.util.Date;
//
//import javax.mail.MessagingException;
//
//import jp.ac.tokushima_u.is.ll.service.WeeklyNotificationService;
//import jp.ac.tokushima_u.is.ll.util.Digests;
//
//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import freemarker.template.TemplateException;
//
//@Controller
//@RequestMapping("/systemschedule")
//public class SystemScheduleController {
//
//	private static final String controllerKey = "a41a571f717e4de0b660a03999fea590936bc0f16a544305a5157cff2ebccfb4101790bab32142baac8dc4b71ede9ccb62aac4f8f7b44beeb3f85910795f40be";
//	private static Logger logger = LoggerFactory.getLogger(SystemScheduleController.class);
//	
//	@Autowired
//	private WeeklyNotificationService weeklyNotificationService;
//	
//	/*
//	@RequestMapping(method = RequestMethod.GET)
//	public String start(){
//		try {
//			weeklyNotificationService.sendNotification();
//		} catch (TemplateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
//		return "success";
//	}
//	*/
//	
//	@RequestMapping(method = RequestMethod.POST)
//	@ResponseBody
//	public String startJob(String name, String key, Long stamp) {
//		if (StringUtils.isBlank(name))
//			return "name_empty";
//		if (StringUtils.isBlank(key))
//			return "key_empty";
//		if (stamp == null || stamp == 0l)
//			return "stamp_empty";
//		
//		Date date = new Date(stamp);
//		String testKey = Hex.encodeHexString(Digests.sha1((name + controllerKey).getBytes(), date.toString().getBytes()));
//		if (testKey.equals(key)) {
//			// DO job
//			if(name.equals("weeklyNotification")){
//				try {
//					weeklyNotificationService.sendNotification();
//				} catch (TemplateException e) {
//					logger.error("Template Exception when sending weekly notification", e);
//					return "template_error";
//				} catch (IOException e) {
//					logger.error("IOException when sending weekly notification", e);
//					return "ioexception";
//				} catch (MessagingException e) {
//					logger.error("MessagingException when sending weekly notification", e);
//					return "messagingexception";
//				}
//			}else{
//				return name+"_not_found";
//			}
//			return "success";
//		} else {
//			return "key_error";
//		}
//	}
//}
