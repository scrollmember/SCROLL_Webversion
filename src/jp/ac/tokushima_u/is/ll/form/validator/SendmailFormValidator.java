package jp.ac.tokushima_u.is.ll.form.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jp.ac.tokushima_u.is.ll.form.SendmailForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Vstar
 */
public class SendmailFormValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return SendmailForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "sendmailForm.email.empty");
        SendmailForm form = (SendmailForm) target;
        if (!StringUtils.isBlank(form.getEmail())) {
            Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(form.getEmail());
            if(!matcher.matches()){
                errors.rejectValue("email", "sendmailForm.email.invalid");
            }
        }
    }
}
