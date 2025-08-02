package br.inatel.pos.dm11.vfu.api.user.controller;

import br.inatel.pos.dm11.vfu.api.core.ApiException;
import br.inatel.pos.dm11.vfu.api.core.AppError;
import br.inatel.pos.dm11.vfu.api.user.UserRequest;
import br.inatel.pos.dm11.vfu.api.user.UserResponse;
import br.inatel.pos.dm11.vfu.api.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/valefood/users")
public class UserController {
    private final UserRequestValidator validator;
    private final UserService service;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public UserController(UserRequestValidator validator, UserService service) {
        this.validator = validator;
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.debug("Received request to list all users.");
        var response = service.searchUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value="/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("userId") String id) throws ApiException {
        log.debug("Received request to list an user by Id: {}", id);
        var response = service.searchUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> postUser(
            @RequestBody UserRequest request,
            BindingResult bindingResult) throws ApiException {
        log.debug("Received request to create a new user...");

        validateUserRequest(request, bindingResult);
        var response = service.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(value="/{userId}")
    public ResponseEntity<UserResponse> putUser(
            @RequestBody UserRequest request,
            @PathVariable("userId") String userId,
            BindingResult bindingResult) throws ApiException {
        log.debug("Received request to update an user");

        validateUserRequest(request, bindingResult);

        var response = service.updateUser(request, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(value="/{userId}")
    public ResponseEntity<List<UserResponse>> deleteUser(@PathVariable("userId") String id) {
        log.debug("Received request to delete an user. Id: {}", id);
        service.removeUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }

    private void validateUserRequest(UserRequest request, BindingResult bindingResult) throws ApiException {
        ValidationUtils.invokeValidator(validator, request, bindingResult);
        if(bindingResult.hasErrors()) {
            var errors = bindingResult.getFieldErrors().stream().map(fe -> new AppError(fe.getCode(), fe.getDefaultMessage())).toList();
            throw new ApiException(HttpStatus.BAD_REQUEST, errors);
        }
    }
}
