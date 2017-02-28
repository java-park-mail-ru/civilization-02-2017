package sample.auth.utils;

import org.apache.commons.validator.routines.EmailValidator;
import sample.auth.ErrorState;
import sample.auth.models.ErrorResponse;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestValidator {

    public static Optional<ErrorResponse> validateNotAuthorizedSession(HttpSession session) {
        if (session.getAttribute(session.getId()) != null) {
            return Optional.of(new ErrorResponse("User already authorized in this session", ErrorState.FORBIDDEN));
        }
        return Optional.empty();
    }

    public static Optional<ErrorResponse> validateAlreadyAuthorizedSession(HttpSession session) {
        if (session.getAttribute(session.getId()) == null) {
            return Optional.of(new ErrorResponse("User not authorized in this session!", ErrorState.FORBIDDEN));
        }
        return Optional.empty();
    }

    public static boolean isValidEmailAddress(String email) {
        final boolean allowLocalAddresses = true;
        return EmailValidator.getInstance(allowLocalAddresses).isValid(email);
    }
}
