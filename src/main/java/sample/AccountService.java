package sample;

import com.msiops.ground.either.Either;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sample.auth.ErrorState;
import sample.auth.dao.UserDAO;
import sample.auth.models.ChangePasswordCredentials;
import sample.auth.models.ErrorResponse;
import sample.auth.models.AuthorizationCredentials;
import sample.auth.models.User;

import java.util.Optional;

import static sample.auth.utils.RequestValidator.isValidEmailAddress;

@Service
public class AccountService {
    /**
     * @return possible registration errors
     */
    public Optional<ErrorResponse> register(AuthorizationCredentials credentials) {
        if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getEmail())
                || StringUtils.isEmpty(credentials.getPassword())) {
            return Optional.of(new ErrorResponse("login, password, and email should be non-empty!", ErrorState.BAD_REQUEST));
        } else if (!credentials.getLogin().matches("^[a-zA-Z0-9\\-_]+$")) {
            return Optional.of(new ErrorResponse("login should contain only latin letters or digits!", ErrorState.BAD_REQUEST));
        } else if (UserDAO.load(credentials.getLogin()).isPresent()) {
            return Optional.of(new ErrorResponse("User with that login already exists!", ErrorState.CONFLICT));
        } else if (!isValidEmailAddress(credentials.getEmail())) {
            return Optional.of(new ErrorResponse("Invalid e-mail format!", ErrorState.BAD_REQUEST));
        }
        final User newUser = new User(credentials.getLogin(), credentials.getPassword(), credentials.getEmail());
        UserDAO.save(newUser);
        return Optional.empty();
    }

    /**
     * @return possibly existing User
     */

    public Either<User, ErrorResponse> loadUser(String login) {
        final Optional<User> loaded = UserDAO.load(login);
        if (loaded.isPresent()) {
            return Either.left(loaded.get());
        }
        return Either.right(new ErrorResponse("Incorrect login", ErrorState.CONFLICT));
    }

    public Either<User, ErrorResponse> loginUser(AuthorizationCredentials credentials) {
        final User userFromDB;
        if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getPassword())) {
            return Either.right(new ErrorResponse("login and password should be non-empty!", ErrorState.BAD_REQUEST));
        } else {
            final Optional<User> loaded = UserDAO.load(credentials.getLogin());
            if (!loaded.isPresent()) {
                return Either.right(new ErrorResponse("User with that login does not exist", ErrorState.CONFLICT));
            } else userFromDB = loaded.get();
        }
        if (!userFromDB.getPassword().equals(credentials.getPassword())) {
            return Either.right(new ErrorResponse("Incorrect password!", ErrorState.CONFLICT));
        }
        return Either.left(userFromDB);
    }

    public Optional<ErrorResponse> changePassword(ChangePasswordCredentials credentials) {
        if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getNewPassword())
                || StringUtils.isEmpty(credentials.getPassword())) {
            return Optional.of(new ErrorResponse("login, password, and new password should be non-empty!", ErrorState.BAD_REQUEST));
        } else {
            final Optional<User> userFromDB = UserDAO.load(credentials.getLogin());
            if (userFromDB.isPresent()) {
                if (!userFromDB.get().getPassword().equals(credentials.getPassword())) {
                    return Optional.of(new ErrorResponse("Incorrect password!", ErrorState.CONFLICT));
                }
            } else {
                return Optional.of(new ErrorResponse("User with that login does not exist", ErrorState.CONFLICT));
            }
            UserDAO.updatePassword(credentials.getLogin(), credentials.getNewPassword());
        }
        return Optional.empty(); //success
    }
    //we need more methods
}
