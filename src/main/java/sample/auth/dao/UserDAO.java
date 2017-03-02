package sample.auth.dao;

import sample.auth.models.User;
import sample.auth.utils.AuthorizationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//В дальнейшем здесь будем доставать из БД
public class UserDAO {
    //<login, User>
    private static Map<String, User> registeredUsers = new HashMap<>();

    //TODO delete this
    static {
        registeredUsers.put("test-user", new User("test-user", "test-password","test_email@test.ru"));
    }

    public static void save(User user){
        registeredUsers.put(user.getLogin(), user);
    }
    public static Optional<User> load(String login){
        return Optional.ofNullable(registeredUsers.get(login));
    }
    public static void updatePassword(String login, String newPassword){
        final User user = registeredUsers.get(login);
        user.setPassword(newPassword);
        registeredUsers.put(login,user); //overwrite old
    }
}
