package com.hexandria.auth.common.user;


import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.ErrorResponse;
import com.msiops.ground.either.Either;

import java.util.List;

public interface IUserManager {
    void updateUser(UserEntity userEntity);
    UserEntity getUserById(String id);

    /**
     *
     * @param userEntity - data input
     * @return created entity
     */
    UserEntity createUser(UserEntity userEntity);

    /**
     *
     * @param login - user login
     * @return User data or null
     */
    UserEntity getUserByLogin(String login);

    /**
     *
     * @param credentials - data input
     * @return sucessfully loaded user data or errors
     */
    Either<UserEntity, List<ErrorResponse>> login(AuthData credentials);
}
