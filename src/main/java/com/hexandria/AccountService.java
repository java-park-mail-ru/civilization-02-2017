package com.hexandria;

import com.msiops.ground.either.Either;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.auth.dao.UserDAO;
import com.hexandria.auth.ErrorState;
import com.hexandria.auth.common.ErrorResponse;


import java.util.ArrayList;
import java.util.List;

@Service
@Deprecated
public class AccountService {
    private UserDAO userDAO;

    /**
     * @return possibly existing UserEntity
     */

    public Either<UserEntity, List<ErrorResponse>> loadUser(String login) {
        final UserEntity loaded = userDAO.load(login);
        if (loaded != null) {
            return Either.left(loaded);
        }
        final List<ErrorResponse> errors = new ArrayList<>();
        errors.add(new ErrorResponse("Incorrect login", ErrorState.FORBIDDEN));
        return Either.right(errors);
    }
    public Either<UserEntity, List<ErrorResponse>> loginUser(AuthData credentials) {
        final List<ErrorResponse> errors = new ArrayList<>();
        if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getPassword())) {
            errors.add(new ErrorResponse("login and password should be non-empty!", ErrorState.BAD_REQUEST));
        }
        final UserEntity userEntityFromDB = userDAO.load(credentials.getLogin());
        if (userEntityFromDB == null){
            errors.add(new ErrorResponse("User with that login does not exist", ErrorState.FORBIDDEN));
        } else if (!userEntityFromDB.getPassword().equals(credentials.getPassword())) {
            errors.add(new ErrorResponse("Incorrect password!", ErrorState.FORBIDDEN));
        }
        if (!errors.isEmpty()){
            return Either.right(errors);
        }
        //noinspection ConstantConditions
        return Either.left(userEntityFromDB); //wont be reached if null
    }

    @NotNull
    public List<ErrorResponse> changePassword(ChangePasswordData credentials) {

        final ArrayList<ErrorResponse> errors = new ArrayList<>();
        if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getNewPassword())
                || StringUtils.isEmpty(credentials.getPassword())) {
            errors.add(new ErrorResponse("Empty credentials", ErrorState.BAD_REQUEST));
        }
        if (!StringUtils.isEmpty(credentials.getLogin())) {
            final UserEntity userEntityFromDB = userDAO.load(credentials.getLogin());
            if (userEntityFromDB != null) {
                if (!userEntityFromDB.getPassword().equals(credentials.getPassword())
                        && StringUtils.isEmpty(credentials.getNewPassword())) {
                    errors.add(new ErrorResponse("Incorrect password!", ErrorState.FORBIDDEN));
                }
            } else {
                errors.add(new ErrorResponse("No such user", ErrorState.FORBIDDEN));
            }
        }

        if (errors.isEmpty()) {
            userDAO.updatePassword(credentials.getLogin(), credentials.getNewPassword());
        }

        return errors;
    }

    public AccountService(@NotNull UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
