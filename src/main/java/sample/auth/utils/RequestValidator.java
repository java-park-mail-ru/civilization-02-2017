package sample.auth.utils;

import sample.auth.ERRORSTATE;
import sample.auth.models.ErrorResponse;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestValidator {

    public static Optional<ErrorResponse> validateNotAuthorizedSession(HttpSession session) {
        if (session.getAttribute(session.getId()) != null) {
            return Optional.of(new ErrorResponse("User already authorized in this session", ERRORSTATE.FORBIDDEN));
        }
        return Optional.empty();
    }
    public static Optional<ErrorResponse> validateAlreadyAuthorizedSession(HttpSession session) {
        if (session.getAttribute(session.getId()) == null) {
            return Optional.of(new ErrorResponse("User not authorized in this session!", ERRORSTATE.FORBIDDEN));
        }
        return Optional.empty();
    }

    public static boolean isValidEmailAddress(String email) {
        final String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        final Pattern p = Pattern.compile(ePattern);
        final Matcher m = p.matcher(email);
        return m.matches();
    }
}
