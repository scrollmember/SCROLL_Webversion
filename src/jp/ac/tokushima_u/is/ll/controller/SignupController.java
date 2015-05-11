package jp.ac.tokushima_u.is.ll.controller;

import java.util.List;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.exception.NotFoundException;
import jp.ac.tokushima_u.is.ll.form.SendmailForm;
import jp.ac.tokushima_u.is.ll.form.SignupForm;
import jp.ac.tokushima_u.is.ll.form.validator.SendmailFormValidator;
import jp.ac.tokushima_u.is.ll.form.validator.SignupFormValidator;
import jp.ac.tokushima_u.is.ll.service.LanguageService;
import jp.ac.tokushima_u.is.ll.service.SignupService;
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
 * @author vstar
 */
@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private LanguageService languageService;
    @Autowired
    private SignupService signupService;
    @Autowired
    private UserService userService;

    @ModelAttribute("langList")
    public List<Language> populateLanguageList() {
        return languageService.searchAllLanguage();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "redirect:/signup/sendmail";
    }

    @RequestMapping(value = "/sendmail", method = RequestMethod.GET)
    public String sendmail(@ModelAttribute("emailForm") SendmailForm form) {
        return "signup/sendmail";
    }

    @RequestMapping(value = "/sendmail", method = RequestMethod.POST)
    public String doSendmail(@ModelAttribute("emailForm") SendmailForm form, BindingResult errors, ModelMap model) {
        new SendmailFormValidator().validate(form, errors);
        if (errors.hasErrors()) {
            return "signup/sendmail";
        }
        Users user = userService.findByEmail(form.getEmail());
        if (user != null && user.getEnabled()) {
            errors.rejectValue("email", "sendmailForm.email.alreadyExist");
            return "signup/sendmail";
        }
        signupService.registerNewUser(form.getEmail());
        return "signup/sendmailResult";
    }

    @RequestMapping(value = "/{activecode}", method = RequestMethod.GET)
    public String profile(@PathVariable String activecode, @ModelAttribute("signupForm") SignupForm signupForm, ModelMap model) {
        Users user = signupService.getUserByActivecode(activecode);
        if (user == null || user.getEnabled()) {
            return "redirect:/signup";
        }
        model.addAttribute("user", user);
        return "signup/signupform";
    }

    @RequestMapping(value = "/{activecode}", method = RequestMethod.POST)
    public String submitProfile(@PathVariable String activecode, @ModelAttribute("signupForm") SignupForm signupForm, BindingResult errors, ModelMap model) {
        Users user = signupService.getUserByActivecode(activecode);
        if (user == null || user.getEnabled()) {
            return "redirect:/signup";
        }
        model.addAttribute("user", user);
        new SignupFormValidator().validate(signupForm, errors);
        if (errors.hasErrors()) {
            return "signup/signupform";
        }
        signupService.addProfile(user, signupForm);
        return "signup/signupformSuccess";
    }
    
    @RequestMapping(value="/resetpassword", method=RequestMethod.GET)
    public String resetpasswordPage(@ModelAttribute("emailForm") SendmailForm form){
    	return "signup/resetpasswordInput";
    }
    
    @RequestMapping(value="/resetpassword", method=RequestMethod.POST)
    public String resetpasswordReceive(@ModelAttribute("emailForm") SendmailForm form, BindingResult errors){
        new SendmailFormValidator().validate(form, errors);
        if (errors.hasErrors()) {
        	return "signup/resetpasswordInput";
        }

        try {
			this.signupService.sendResetPasswordMail(form.getEmail());
		} catch (NotFoundException e) {
			if("email".equals(e.getMessage())){
	        	errors.rejectValue("email", "sendmailForm.email.emailNotExist");
	        	return "signup/resetpasswordInput";
			}else{
				e.printStackTrace();
			}
		}
    	return "signup/resetpasswordResult";
    }
    
    @RequestMapping(value="/resetpassword/{activecode}", method=RequestMethod.GET)
    public String resetpasswordActive(@PathVariable String activecode, @ModelAttribute("signupForm") SignupForm signupForm, ModelMap model){
        Users user = signupService.getUserByActivecode(activecode);
        if (user == null) {
        	model.addAttribute("message", "Active code is not exist");
            return "signup/resetpasswordError";
        }
        model.addAttribute("user", user);
    	return "signup/resetpasswordForm";
    }
    
    @RequestMapping(value="/resetpassword/{activecode}", method=RequestMethod.POST)
    public String resetpasswordActiveReceive(@PathVariable String activecode, @ModelAttribute("signupForm") SignupForm form, BindingResult errors, ModelMap model){
        Users user = signupService.getUserByActivecode(activecode);
        if (user == null) {
        	model.addAttribute("message", "Active code is not exist");
            return "signup/resetpasswordError";
        }
        model.addAttribute("user", user);
    	
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "signupForm.password.empty");

        if (!StringUtils.isBlank(form.getPassword()) && !form.getPassword().equals(form.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "signupForm.passwordConfirm.notSame");
        }

        if (!StringUtils.isBlank(form.getPassword()) && (form.getPassword().length() < SignupFormValidator.PASSWORD_MIN_LENGTH || form.getPassword().length() > SignupFormValidator.PASSWORD_MAX_LENGTH)) {
            errors.rejectValue("password", "signupForm.password.lengthError", new Object[]{SignupFormValidator.PASSWORD_MIN_LENGTH, SignupFormValidator.PASSWORD_MAX_LENGTH, form.getPassword().length()}, null);
        }
    	
        if(errors.hasErrors()){
        	return "signup/resetpasswordForm";
        }
        
        this.signupService.resetPassword(user, form.getPassword());
        
    	return "signup/resetpasswordSuccess";
    }
    
    public String unsubscribe(){
    	return "";
    }
}
