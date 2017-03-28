package com.hexandria.auth.common.user;


import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.ErrorResponse;
import com.msiops.ground.either.Either;

import java.util.List;

public interface IUserManager {
    UserEntity updateUser(UserEntity userEntity);
    List<UserEntity> updateUsers(List<UserEntity> userEntities);
    void changeUserPassword(ChangePasswordData credentials);
    UserEntity getUserById(String id);

    /**
     *
     * @param credentials - data input
     * @return possible registration errors
     */
    List<ErrorResponse> register(AuthData credentials);

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
