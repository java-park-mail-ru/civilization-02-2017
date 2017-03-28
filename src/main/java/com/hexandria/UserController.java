package com.hexandria;

import com.msiops.ground.either.Either;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.ErrorResponse;
import com.hexandria.auth.common.SuccessResponseMessage;
import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.auth.common.user.UserManager;
import com.hexandria.auth.utils.RequestValidator;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin // for localhost usage
//@CrossOrigin(origins = "https://[...].herokuapp.com") //for remote usage
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @NotNull
    private final AccountService accountService;
    @NotNull
    private final UserManager userManager;
    @RequestMapping(path = "api/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity register(@RequestBody AuthData credentials, HttpSession httpSession) {
        logger.debug("/signup called with login: {}", credentials.getLogin());
        final ErrorResponse sessionError = RequestValidator.validateNotAuthorizedSession(httpSession);
        if (sessionError != null) {
            return buildErrorResponse(sessionError);
        }
        final List<ErrorResponse> registrationError = userManager.register(credentials);
        if (!registrationError.isEmpty()){ // if errors returned
            return buildErrorResponse(registrationError);
        }
        return ResponseEntity.ok(new SuccessResponseMessage("Successfully registered user"));
    }

    @RequestMapping(path = "/api/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity login(@RequestBody AuthData credentials, HttpSession httpSession) {
        logger.debug("/login called with login: {}", credentials.getLogin());
        final ErrorResponse sessionError = RequestValidator.validateNotAuthorizedSession(httpSession);
        if (sessionError !=null) {
            return buildErrorResponse(sessionError);
        }
        final Either<UserEntity, List<ErrorResponse>> result = userManager.login(credentials);
        if (!result.isLeft()){ //if error
            return buildErrorResponse(result.getRight());
        }
        final UserEntity userEntity = result.getLeft();
        httpSession.setAttribute(httpSession.getId(), userEntity.getLogin());
        return ResponseEntity.ok(new SuccessResponseMessage("Successfully authorized user " + userEntity.getLogin()));
    }

    @RequestMapping(path = "/api/logout", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity logout(HttpSession httpSession) {
        logger.debug("/logout called for id: {}", httpSession.getId());
        final ErrorResponse sessionError = RequestValidator.validateAlreadyAuthorizedSession(httpSession);
        if (sessionError != null) {
            return buildErrorResponse(sessionError);
        }
        httpSession.removeAttribute(httpSession.getId());
        return ResponseEntity.ok(new SuccessResponseMessage("User successfully logged out"));
    }

    // change password
    @RequestMapping(path = "/api/user", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity changePassword(@RequestBody ChangePasswordData credentials, HttpSession httpSession) {
        logger.debug("/user-change-pass called");
        final ErrorResponse sessionError = RequestValidator.validateAlreadyAuthorizedSession(httpSession);
        if (sessionError !=null) {
            return buildErrorResponse(sessionError);
        }
        final List<ErrorResponse> passwordChangeErrors = accountService.changePassword(credentials);
        if (!passwordChangeErrors.isEmpty()) {
            return buildErrorResponse(passwordChangeErrors);
        }
        return ResponseEntity.ok(new SuccessResponseMessage("Successfully changed password for user "+credentials.getLogin()));
    }
    //get logged user data
    @RequestMapping(path = "/api/user", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public ResponseEntity getCurrentUser(HttpSession httpSession){
        final ErrorResponse sessionError = RequestValidator.validateAlreadyAuthorizedSession(httpSession);
        if (sessionError != null) {
            return buildErrorResponse(sessionError);
        }
        final String login = String.valueOf(httpSession.getAttribute(httpSession.getId())); //get login from session, 100% not null
        final Either<UserEntity, List<ErrorResponse>> result = accountService.loadUser(login);
        if (!result.isLeft()){
            return buildErrorResponse(result.getRight());
        }
        return ResponseEntity.ok(result.getLeft());
    }
    //get logged user data
    @RequestMapping(path = "/api/delete")
    public ResponseEntity deleteUser(){
        int a  = 0;
        //userManager.createUser(new AuthData("ABc", "abc", "abc@abc.ru"));
            return ResponseEntity.ok("");
    }

    private ResponseEntity buildErrorResponse(List<ErrorResponse> errors) {
        final StringBuilder errorString = new StringBuilder();
        for(ErrorResponse e : errors){
            errorString.append(e.getErrorText());
            errorString.append(',');
        }
        final JSONObject result = new JSONObject();
        final String error = errorString.toString();
        result.put("Errors", error);
        return ResponseEntity.status(errors.get(0).getErrorStatus().getCode()).body(result);
    }
    private ResponseEntity buildErrorResponse(ErrorResponse error) {
        return ResponseEntity.status(error.getErrorStatus().getCode()).body(error);
    }

    public UserController(@NotNull AccountService accountService, @NotNull UserManager userManager) {
        this.accountService = accountService;
        this.userManager = userManager;
    }
}
