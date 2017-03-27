package sample.auth.common.user;


import sample.auth.common.ChangePasswordCredentials;

import java.util.List;

public interface IUserManager {
    User updateUser(User user);
    List<User> updateUsers(List<User> users);
    void changeUserPassword(ChangePasswordCredentials credentials);
    User getUserById(String id);
}
