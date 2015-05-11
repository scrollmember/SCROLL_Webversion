package jp.ac.tokushima_u.is.ll.controller;

import jp.ac.tokushima_u.is.ll.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author houbin
 */
@Controller
@RequestMapping("/signin")
public class SigninController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/signin",method=RequestMethod.GET)
    public String execute(){
    	if(SecurityUtils.getSubject()!=null && SecurityUtils.getSubject().isAuthenticated()){
    		return "redirect:/";
    	}
        userService.initSystemUser();
        return "login";
    }
    
    @RequestMapping(value="/signin",method=RequestMethod.POST)
    public String signin(String username, String password, Boolean remember, ModelMap model){
    	if(remember==null) remember = false;
    	AuthenticationToken token = new UsernamePasswordToken(username, password, remember);
    	try {
    		
			SecurityUtils.getSubject().login(token);
		} catch (AuthenticationException e) {
			model.addAttribute("error", "The E-mail or the password is incorrect");
			return "login";
		}
        return "redirect:/";
    }
}
