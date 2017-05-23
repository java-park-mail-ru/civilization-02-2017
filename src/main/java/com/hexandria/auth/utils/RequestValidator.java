package com.hexandria.auth.utils;

import com.hexandria.auth.ErrorState;
import com.hexandria.auth.common.ErrorResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpSession;

public class RequestValidator {

    @Nullable
    public static ErrorResponse validateNotAuthorizedSession(HttpSession session) {
        if (session.getAttribute(session.getId()) != null) {
            return new ErrorResponse("GameAvatar already authorized in this session", ErrorState.FORBIDDEN);
        }
        return null;
    }
    @Nullable
    public static ErrorResponse validateAlreadyAuthorizedSession(HttpSession session) {
        if (session.getAttribute(session.getId()) == null) {
            return new ErrorResponse("GameAvatar not authorized in this session!", ErrorState.FORBIDDEN);
        }
        return null;
    }

    public static boolean isValidEmailAddress(String email) {
        final boolean allowLocalAddresses = true;
        return EmailValidator.getInstance(allowLocalAddresses).isValid(email);
    }
}
