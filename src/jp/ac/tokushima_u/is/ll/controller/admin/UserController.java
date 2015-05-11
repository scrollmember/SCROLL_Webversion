package jp.ac.tokushima_u.is.ll.controller.admin;

import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.UserSearchCondForm;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author Houbin
 */
@Controller
@RequestMapping("/admin/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(String email, @ModelAttribute("searchCond") UserSearchCondForm form,
			ModelMap model) {
		String searchMail = "";
		if(!StringUtils.isBlank(email)){
			searchMail = email;
		}
		model.addAttribute("userList", userService.findAllUserList(searchMail));
		return "admin/user/list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable String id, ModelMap model) {
		Users user = userService.getById(id);
		model.addAttribute("user", user);
		return "admin/user/show";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable String id, ModelMap model) {
		model.addAttribute("user", userService.getById(id));
		return "admin/user/edit";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	public String update(@PathVariable String id, @ModelAttribute("user")Users user, BindingResult result, String password, String passwordConfirm, ModelMap model) {
		Users u = userService.getById(id);
		if(u==null)return "redirect:/admin/user/" + id;
		ValidationUtils.rejectIfEmptyOrWhitespace(result, "pcEmail", "user.email.empty", "Please input pc email");
		if(!StringUtils.isBlank(password) && !password.equals(passwordConfirm)){
			result.rejectValue("password", "user.password.notsame", "Different");
		}
		if(result.hasErrors()){
			return "admin/user/add";
		}
		if(!StringUtils.isBlank(password)){
			user.setPassword(password);
		}
		user.setId(id);
		userService.editByAdmin(user);
		return "redirect:/admin/user/" + id;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("user", new Users());
		return "/admin/user/add";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("user")Users user,BindingResult result,  String password, String passwordConfirm, ModelMap model) {
		ValidationUtils.rejectIfEmptyOrWhitespace(result, "pcEmail", "user.email.empty", "Please input pc email");
		ValidationUtils.rejectIfEmptyOrWhitespace(result, "password", "user.password.empty", "Please input password");
		if(StringUtils.isBlank(passwordConfirm)){
			result.rejectValue("password", "user.passwordConfirm", "Please confirm password");
		}
		if(!StringUtils.isBlank(password) && !password.equals(passwordConfirm)){
			result.rejectValue("password", "user.password.notsame", "Different");
		}
		if(result.hasErrors()){
			return "admin/user/add";
		}
		user.setPassword(password);
		userService.createByAdmin(user);
		return "redirect:/admin/user";
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserService getUserService() {
		return userService;
	}
}