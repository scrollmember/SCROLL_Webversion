package jp.ac.tokushima_u.is.ll.controller.api;
//TODO Delete
//
//import java.io.IOException;
//
//import jp.ac.tokushima_u.is.ll.entity.Item;
//import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
//import jp.ac.tokushima_u.is.ll.service.ItemService;
//import jp.ac.tokushima_u.is.ll.service.PropertyService;
//import jp.ac.tokushima_u.is.ll.service.UserService;
//import jp.ac.tokushima_u.is.ll.service.WeeklyNotificationService;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//@Controller
//@RequestMapping("/api/imageupload")
//public class ImageUploadController {
//	
//    @Autowired
//    private ItemService itemService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private WeeklyNotificationService weeklyNotificationService;
//    @Autowired
//    private PropertyService propertyService;
//	
//    @RequestMapping(method=RequestMethod.POST)
//    @ResponseBody
//    public String uploadImage(@RequestParam("image") MultipartFile image, @RequestParam("username") String email, @RequestParam("password") String password, ModelMap model){
//    	if(StringUtils.isEmpty(email)){
//    		return "error_email_empty";
//    	}
//    	
//    	if(StringUtils.isBlank(password)){
//    		return "error_password_empty";
//    	}
//    	
//    	if(image == null || image.getSize()==0) {
//    		return "error_image_empty";
//    	}
//    	
//    	//Do Login
//    	try {
//			SecurityUtils.getSubject().login(new UsernamePasswordToken(email, password, true));
//		} catch (AuthenticationException e1) {
//			return "error_login_failed";
//		}
//
//    	try {
//			Item item = this.itemService.uploadImageFirst(image);
//			if(item!=null){
//				return weeklyNotificationService.encodeUrl(propertyService.getSystemUrl()+"/item/"+item.getId()+"/edit", SecurityUserHolder.getCurrentUser());
//			}
//			return "error_item_create";
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "error_ioexception";
//		}
//    	
//    }
//}
