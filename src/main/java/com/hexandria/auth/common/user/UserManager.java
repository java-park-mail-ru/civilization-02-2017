package com.hexandria.auth.common.user;


import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.ErrorResponse;
import com.msiops.ground.either.Either;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserManager {
    void updateUser(UserEntity userEntity);

    @Nullable
    UserEntity getUserById(Integer id);

    /**
     * @param userEntity - data input
     * @return created entity
     */
    @Nullable
    UserEntity createUser(UserEntity userEntity);

    /**
     * @param login - user login
     * @return User data or null
     */
    @Nullable
    UserEntity getUserByLogin(String login);

    /**
     * @param credentials - data input
     * @return sucessfully loaded user data or errors
     */
    @NotNull
    Either<UserEntity, List<ErrorResponse>> login(AuthData credentials);

    @NotNull
    List<ErrorResponse> changeUserPassword(ChangePasswordData credentials);

    @NotNull
    List<ErrorResponse> register(AuthData credentials);
}
