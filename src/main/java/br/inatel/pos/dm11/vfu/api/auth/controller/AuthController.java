package br.inatel.pos.dm11.vfu.api.auth.controller;

import br.inatel.pos.dm11.vfu.api.auth.AuthRequest;
import br.inatel.pos.dm11.vfu.api.auth.AuthResponse;
import br.inatel.pos.dm11.vfu.api.auth.service.AuthService;
import br.inatel.pos.dm11.vfu.api.core.ApiException;
import br.inatel.pos.dm11.vfu.api.core.AppError;
import br.inatel.pos.dm11.vfu.api.user.UserRequest;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/valefood/auth")
public class AuthController {

    private static final Logger log = (Logger) LoggerFactory.getLogger(AuthController.class);

    private final AuthRequestValidator validator;
    private final AuthService service;


    public AuthController(AuthRequestValidator validator, AuthService service) {
        this.validator = validator;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest req, BindingResult bindingResult){
        log.info("Trying to authenticate...");

        var res = service.authenticate(req);

        return ResponseEntity.status((HttpStatus.OK)).body(res);
    }

    private void validateRequest(AuthRequest req, BindingResult bindingResult) throws ApiException {
        ValidationUtils.invokeValidator(validator, req, bindingResult);
        if(bindingResult.hasErrors()) {
            var errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fe -> new AppError(fe.getCode(), fe.getDefaultMessage()))
                    .toList();
            throw new ApiException(HttpStatus.BAD_REQUEST, errors);
        }
    }
}
