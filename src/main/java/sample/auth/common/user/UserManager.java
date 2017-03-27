package sample.auth.common.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.auth.common.ChangePasswordCredentials;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class UserManager implements IUserManager{

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    @PersistenceContext(unitName = "hexandria")
    protected EntityManager em;

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public List<User> updateUsers(List<User> users) {
        return null;
    }

    @Override
    public void changeUserPassword(ChangePasswordCredentials credentials) {

    }

    @Override
    public User getUserById(String id) {
        return null;
    }
}
