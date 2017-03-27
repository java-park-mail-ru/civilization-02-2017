package com.hexandria.auth.common.user;


import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.ErrorResponse;

import java.util.List;

public interface IUserManager {
    UserEntity updateUser(UserEntity userEntity);
    List<UserEntity> updateUsers(List<UserEntity> userEntities);
    void changeUserPassword(ChangePasswordData credentials);
    UserEntity getUserById(String id);
    UserEntity createUser(UserEntity entity);
    List<ErrorResponse> register(AuthData credentials);
    UserEntity getUserByLogin(String login);
}
