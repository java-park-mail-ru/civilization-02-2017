package sample.auth.dao;

import org.jetbrains.annotations.Nullable;
import sample.auth.models.User;

import java.util.HashMap;
import java.util.Map;

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
    @Nullable
    public static User load(String login){
        return registeredUsers.get(login);
    }
    public static void updatePassword(String login, String newPassword){
        final User user = registeredUsers.get(login);
        user.setPassword(newPassword);
        registeredUsers.put(login,user); //overwrite old
    }
}
