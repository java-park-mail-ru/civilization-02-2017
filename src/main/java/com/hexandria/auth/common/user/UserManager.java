package com.hexandria.auth.common.user;


import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.ErrorResponse;
import com.msiops.ground.either.Either;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserManager {
    void updateUser(@NotNull UserEntity userEntity);

    @Nullable
    UserEntity getUserById(@NotNull Integer id);

    /**
     * @param userEntity - data input
     * @return created entity
     */
    @Nullable
    UserEntity createUser(@NotNull UserEntity userEntity);

    /**
     * @param login - user login
     * @return User data or null
     */
    @Nullable
    UserEntity getUserByLogin(@NotNull String login);

    /**
     * @param credentials - data input
     * @return sucessfully loaded user data or errors
     */
    @NotNull
    Either<UserEntity, List<ErrorResponse>> login(@NotNull AuthData credentials);

    @NotNull
    List<ErrorResponse> changeUserPassword(@NotNull ChangePasswordData credentials);

    @NotNull
    List<ErrorResponse> register(@NotNull AuthData credentials);
}
