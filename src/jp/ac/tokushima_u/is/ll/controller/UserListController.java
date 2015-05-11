package jp.ac.tokushima_u.is.ll.controller;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/userlist")
@Controller
public class UserListController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping
	public String list(ModelMap model){
		List<Users> userList = userService.findAllUserListNew();
		model.addAttribute("say", "ABCDEFG");
		model.addAttribute("userList", userList);
		return "/userlist/list";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String add(){
		return "/userlist/add";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String create(){
		return "userlist/addSuccess.jsp";
	}
}
