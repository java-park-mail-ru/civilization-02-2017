package com.hexandria.auth.dao;

import com.hexandria.auth.common.user.UserEntity;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//В дальнейшем здесь будем доставать из БД
@Service
public class UserDAO {
    //<login, UserEntity>
    private Map<String, UserEntity> registeredUsers = new HashMap<>();

    @Nullable
    public UserEntity load(String login){
        return registeredUsers.get(login);
    }
    public void updatePassword(String login, String newPassword){
        final UserEntity userEntity = registeredUsers.get(login);
        userEntity.setPassword(newPassword);
        registeredUsers.put(login, userEntity); //overwrite old
    }

    public UserDAO() {
        registeredUsers = new HashMap<>();
        registeredUsers.put("test-user", new UserEntity("test-user", "test-password","test_email@test.ru"));
    }
}
