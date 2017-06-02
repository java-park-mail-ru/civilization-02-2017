package com.hexandria.auth.utils;

import com.hexandria.auth.ErrorState;
import com.hexandria.auth.common.ErrorResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;

public class ValidationUtil {
    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Nullable
    public static ErrorResponse validateNotAuthorizedSession(HttpSession session) {
        if (session.getAttribute(session.getId()) != null) {
            return new ErrorResponse("GamePlayer already authorized in this session", ErrorState.FORBIDDEN);
        }
        return null;
    }

    @Nullable
    public static ErrorResponse validateAlreadyAuthorizedSession(HttpSession session) {
        if (session.getAttribute(session.getId()) == null) {
            return new ErrorResponse("GamePlayer not authorized in this session!", ErrorState.FORBIDDEN);
        }
        return null;
    }

    public static boolean isValidEmailAddress(String email) {
        final boolean allowLocalAddresses = true;
        return EmailValidator.getInstance(allowLocalAddresses).isValid(email);
    }

    public static String encodePassword(String original) {
        return passwordEncoder.encode(original);
    }
    public static boolean passwordMatches(String userInput, String encodedPassword){
        return passwordEncoder.matches(userInput, encodedPassword);
    }

}
