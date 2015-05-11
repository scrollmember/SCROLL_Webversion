package jp.ac.tokushima_u.is.ll.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.UserInfoForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/userinfo")
public class UserInfoController {
	@Autowired
	private UserService userService;
	
	@RequestMapping
	@ResponseBody
	public String get(ModelMap model, Long update) {
		
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		UserInfoForm userinfo = new UserInfoForm();
		if(update!=null){
			long updated = user.getUpdateTime().getTime();
			if(update >= updated){
//				model.addAttribute("userinfo", userinfo);				
				return "";
			}
		}

		userinfo.setUserid(user.getId());
		if (user.getDefaultCategory() != null)
			userinfo.setDefcategory(user.getDefaultCategory().getId());

		userinfo.setNickname(user.getNickname());
		List<Category> categoryList = user.getMyCategoryList();
		if (categoryList != null && categoryList.size() > 0) {
			Map<String, String> cats = new HashMap<String, String>();
			for (Category cat : categoryList) {
				cats.put(cat.getId(), cat.getName());
			}
			userinfo.setCategorys(cats);
		}
		Map<String,String>mylans = new HashMap<String,String>();
		for(Language lan:user.getMyLangs()){
			mylans.put(lan.getId(), lan.getName());
		};
		userinfo.setMylans(mylans);

		Map<String,String>studylans = new HashMap<String,String>();
		for(Language lan:user.getStudyLangs()){
			studylans.put(lan.getId(),lan.getName());
		};
		userinfo.setStudylans(studylans);
		
		userinfo.setUpdate(user.getUpdateTime().getTime());
		
		model.addAttribute("userinfo", userinfo);
		Gson gson = new Gson();
		return gson.toJson(model);
	}
	
	@RequestMapping(value = "/{email}", method = RequestMethod.POST)
	public String getUserinfoByEmail(ModelMap model,@PathVariable String email){
		
		Users user = userService.findByEmail(email);
		
		model.addAttribute("user", user);
		
		Gson gson = new Gson();
	
		return gson.toJson(model);
	}

//	@RequestMapping
//	public String get(ModelMap model,UserInfoForm form){
//		String username = form.getUsername();
//		String password = form.getPassword();
//		UserInfoForm userinfo = new UserInfoForm();
//
//		if(username==null||password==null)
//			userinfo.setLogin_error_code(ErrorCodeConstants.Login_Error_empty_username);
//		else{
//			Users user = this.userService.findByEmail(username);
//			if(user==null||!user.getPassword().equals(passwordEncoder.encodePassword(password,"")))
//				userinfo.setLogin_error_code(ErrorCodeConstants.Login_Error_no_user);
//			else{
//				userinfo.setUserid(user.getId());
//				if(user.getDefaultCategory()!=null)
//					userinfo.setDefcategory(user.getDefaultCategory().getId());
//				
//				userinfo.setNickname(user.getNickname());
//				List<Category> categoryList = user.getMyCategoryList();
//				if(categoryList!=null&&categoryList.size()>0){
//					Map<String,String>cats = new HashMap<String,String>();
//					for(Category cat:categoryList){
//						cats.put(cat.getId(), cat.getName());
//					}
//					userinfo.setCategorys(cats);
//				}
//			}
//		}
//		
//		model.addAttribute("userinfo", userinfo);
//		return "";
//	}
	
}
