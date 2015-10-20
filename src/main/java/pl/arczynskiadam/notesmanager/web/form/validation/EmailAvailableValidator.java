package pl.arczynskiadam.notesmanager.web.form.validation;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import pl.arczynskiadam.notesmanager.core.service.UserService;

public class EmailAvailableValidator implements ConstraintValidator<EmailAvailable, String> {

	@Resource
	private UserService userService;
	
    public void initialize(EmailAvailable constraintAnnotation) {}

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
    	
        if (object == null)
            return true;

        return userService.isEmailAvailable(object);
    }
}