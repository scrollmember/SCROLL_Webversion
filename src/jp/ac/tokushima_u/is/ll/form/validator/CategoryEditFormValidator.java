package jp.ac.tokushima_u.is.ll.form.validator;

import jp.ac.tokushima_u.is.ll.form.CategoryEditForm;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CategoryEditFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CategoryEditForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "categoryEditForm.name.empty");
	}
}
