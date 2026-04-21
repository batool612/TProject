package com.tanmeyah.postoffice.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


@Component
public class EgyptianNationalIdValidator implements ConstraintValidator<ValidEgyptianNationalId, String> {

    private static final String REGEX =
            "^(2|3)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])\\d{7}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        return value.length() == 14 && value.matches(REGEX);
    }
}