package br.inatel.pos.dm11.vfu.api.auth.controller;

import br.inatel.pos.dm11.vfu.api.auth.AuthRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AuthRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AuthRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "Email is required!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password is required!");
    }
}
