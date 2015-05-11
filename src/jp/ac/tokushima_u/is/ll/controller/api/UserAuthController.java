package jp.ac.tokushima_u.is.ll.controller.api;

import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/userauth")
public class UserAuthController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String auth(@RequestParam("email") String email, @RequestParam("password") String password){
		if(StringUtils.isBlank(email)){
			return "error_email_empty";
		}
		if(StringUtils.isBlank(password)){
			return "error_password_empty";
		}
		email = email.trim();
		password = password.trim();
		Users user = userService.validateUser(email, password);
		if(user == null){
			return "error_login_failed";
		}
		return "success";
	}
}
