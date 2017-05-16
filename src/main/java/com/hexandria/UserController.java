package com.hexandria;

import com.hexandria.auth.ErrorState;
import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.ErrorResponse;
import com.hexandria.auth.common.SuccessResponseMessage;
import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.auth.common.user.UserManager;
import com.hexandria.auth.utils.RequestValidator;
import com.msiops.ground.either.Either;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin // for localhost usage
//@CrossOrigin(origins = "https://[...].herokuapp.com") //for remote usage
@RequestMapping(value = "api/user")
@Transactional
public class UserController {

    final Logger logger = LoggerFactory.getLogger(UserController.class);

    @NotNull
    private final UserManager userManager;

    @PostMapping(path = "/signup")
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

    @PostMapping(path = "/login")
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
        httpSession.setAttribute("userId", userEntity.getId());
        return ResponseEntity.ok(new SuccessResponseMessage("Successfully authorized user " + userEntity.getLogin()));
    }

    @PostMapping(path = "/logout")
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
    @PostMapping
    public ResponseEntity changePassword(@RequestBody ChangePasswordData credentials, HttpSession httpSession) {
        logger.debug("/user-change-pass called");
        final ErrorResponse sessionError = RequestValidator.validateAlreadyAuthorizedSession(httpSession);
        if (sessionError !=null) {
            return buildErrorResponse(sessionError);
        }
        final List<ErrorResponse> passwordChangeErrors = userManager.changeUserPassword(credentials);
        if (!passwordChangeErrors.isEmpty()) {
            return buildErrorResponse(passwordChangeErrors);
        }
        return ResponseEntity.ok(new SuccessResponseMessage("Successfully changed password for user "+credentials.getLogin()));
    }
    //get logged user data
    @GetMapping
    public ResponseEntity getCurrentUser(HttpSession httpSession){
        final ErrorResponse sessionError = RequestValidator.validateAlreadyAuthorizedSession(httpSession);
        if (sessionError != null) {
            return buildErrorResponse(sessionError);
        }
        final String login = String.valueOf(httpSession.getAttribute(httpSession.getId())); //get login from session, 100% not null
        final UserEntity result = userManager.getUserByLogin(login);
        if (result == null){
            return buildErrorResponse(new ErrorResponse("Incorrect login", ErrorState.FORBIDDEN));
        }
        return ResponseEntity.ok(result);
    }
    // delete user
    @PostMapping(path = "/delete")
    public ResponseEntity deleteUser(HttpSession httpSession){
        final ErrorResponse sessionError = RequestValidator.validateAlreadyAuthorizedSession(httpSession);
        if (sessionError != null) {
            return buildErrorResponse(sessionError);
        }
        final String userName = (String) httpSession.getAttribute(httpSession.getId());
        userManager.deleteUser(userName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private ResponseEntity buildErrorResponse(List<ErrorResponse> errors) {
        return ResponseEntity.status(errors.get(0).getErrorStatus().getCode()).body(errors);
    }
    private ResponseEntity buildErrorResponse(ErrorResponse error) {
        return ResponseEntity.status(error.getErrorStatus().getCode()).body(error);
    }

    public UserController(@NotNull UserManager userManager) {
        this.userManager = userManager;
    }
}
