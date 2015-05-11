package jp.ac.tokushima_u.is.ll.form.validator;

import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.form.ProfileEditForm;
import jp.ac.tokushima_u.is.ll.form.SignupForm;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author houbin
 */
public class ProfileEditFormValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return SignupForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ProfileEditForm form = (ProfileEditForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickname", "signupForm.nickname.empty");

        if (form.getMyLangs() == null || form.getMyLangs().size() == 0 || StringUtils.isBlank(form.getMyLangs().get(0))) {
            errors.rejectValue("myLangs[0]", "signupForm.myLangs[0].empty");
        }

        if (form.getStudyLangs() == null || form.getStudyLangs().size() == 0 || StringUtils.isBlank(form.getStudyLangs().get(0))) {
            errors.rejectValue("studyLangs[0]", "signupForm.studyLangs[0].empty");
        }
        
        List<String> langList = new ArrayList<String>();
        for(int i=0;i<form.getMyLangs().size();i++){
        	if(!StringUtils.isBlank(form.getMyLangs().get(i)) && langList.contains(form.getMyLangs().get(i))){
        		errors.rejectValue("myLangs["+i+"]", "signupForm.language.duplicate");
        	}
        	langList.add(form.getMyLangs().get(i));
        }
        
        for(int i=0;i<form.getStudyLangs().size();i++){
        	if(!StringUtils.isBlank(form.getStudyLangs().get(i)) && langList.contains(form.getStudyLangs().get(i))){
        		errors.rejectValue("studyLangs["+i+"]", "signupForm.language.duplicate");
        	}
        	langList.add(form.getStudyLangs().get(i));
        }
    }
    
    public void validatePassword(Object target, Errors errors) {
    	ProfileEditForm form = (ProfileEditForm) target;
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldpassword", "profileEditForm.oldpassword.empty");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "signupForm.password.empty");
        if (!StringUtils.isBlank(form.getPassword())){
	        if (!StringUtils.isBlank(form.getPassword()) && !form.getPassword().equals(form.getPasswordConfirm())) {
	            errors.rejectValue("passwordConfirm", "signupForm.passwordConfirm.notSame");
	        }
	
	        if (!StringUtils.isBlank(form.getPassword()) && (form.getPassword().length() < SignupFormValidator.PASSWORD_MIN_LENGTH || form.getPassword().length() > SignupFormValidator.PASSWORD_MAX_LENGTH)) {
	            errors.rejectValue("password", "signupForm.password.lengthError", new Object[]{SignupFormValidator.PASSWORD_MIN_LENGTH, SignupFormValidator.PASSWORD_MAX_LENGTH, form.getPassword().length()}, null);
	        }
        }
    }
}
