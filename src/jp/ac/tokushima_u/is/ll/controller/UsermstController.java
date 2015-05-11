package jp.ac.tokushima_u.is.ll.controller;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author li
 *
 */
@Controller
@RequestMapping("/usermst")
public class UsermstController {
	
	@Autowired
	public UserService userService;
	@RequestMapping
	public String list(ModelMap model){
		List<Users> userlist = userService.searchAllUsersNew();
		
		model.addAttribute("users", userlist);
		return "usermst/list";
	}
}
