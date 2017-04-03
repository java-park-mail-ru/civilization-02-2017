package com.hexandria.auth.common.user;

import com.hexandria.auth.ErrorState;
import com.hexandria.auth.common.AuthData;
import com.hexandria.auth.common.ChangePasswordData;
import com.hexandria.auth.common.ErrorResponse;
import com.hexandria.auth.utils.PersistenceManager;
import com.msiops.ground.either.Either;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static com.hexandria.auth.utils.RequestValidator.isValidEmailAddress;

@Service
public class UserManager implements IUserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    private EntityManager entityManager;

    @Override
    public void updateUser(UserEntity userEntity) {
        final EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(userEntity);
            tx.commit();
        }
        catch (Throwable e){
            tx.rollback();
            throw e;
        }
    }

    @Override
    @NotNull
    public List<ErrorResponse> changeUserPassword(ChangePasswordData credentials) {

        final ArrayList<ErrorResponse> errors = new ArrayList<>();
        if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getNewPassword())
                || StringUtils.isEmpty(credentials.getPassword())) {
            errors.add(new ErrorResponse("Empty credentials", ErrorState.BAD_REQUEST));
        }
        UserEntity user = null;
        if (!StringUtils.isEmpty(credentials.getLogin())) {
            user = getUserByLogin(credentials.getLogin());
            if (user != null) {
                if (!user.getPassword().equals(credentials.getPassword())
                        || StringUtils.isEmpty(credentials.getNewPassword())) {
                    errors.add(new ErrorResponse("Incorrect password!", ErrorState.FORBIDDEN));
                }
            } else {
                errors.add(new ErrorResponse("No such user", ErrorState.FORBIDDEN));
            }
        }

        if (errors.isEmpty()) {
            //noinspection ConstantConditions
            user.setPassword(credentials.getNewPassword()); // errors wont be empty if password is invalid
            updateUser(user);
        }

        return errors;
    }
    @Override
    @Nullable
    public UserEntity getUserById(Integer id) {
        return entityManager.find(UserEntity.class, id);
    }

    @Override
    @Nullable
    public UserEntity getUserByLogin(String login) {
        UserEntity user = null;
        try {
            user = (UserEntity) entityManager.
                    createQuery("select u from UserEntity u where u.login = :login").setParameter("login", login).getSingleResult();
        } catch (NoResultException e) {
            logger.info("no entity found for login {}", login);
        }
        return user;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        final EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(userEntity);
            tx.commit();
        } catch (Throwable e) {
            tx.rollback();
            throw e;
        }
        return userEntity;
    }

    /**
     * @return possible registration errors
     */
    @Override
    @SuppressWarnings("OverlyComplexMethod")
    @NotNull
    public List<ErrorResponse> register(AuthData credentials) {

        final List<ErrorResponse> errors = new ArrayList<>();

        if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getEmail()) || StringUtils.isEmpty(credentials.getPassword())) {
            errors.add(new ErrorResponse("Empty credentials", ErrorState.BAD_REQUEST));
        }
        if (!credentials.getLogin().matches("^[a-zA-Z0-9\\-_]+$") && !StringUtils.isEmpty(credentials.getLogin())) {
            errors.add(new ErrorResponse("Incorrect login", ErrorState.BAD_REQUEST));
        }
        final UserEntity user = getUserByLogin(credentials.getLogin());

        if (user != null && !StringUtils.isEmpty(credentials.getLogin())) {
            errors.add(new ErrorResponse("User exists", ErrorState.FORBIDDEN));
        }
        if (!isValidEmailAddress(credentials.getEmail()) && !StringUtils.isEmpty(credentials.getEmail())) {
            errors.add(new ErrorResponse("Email format", ErrorState.BAD_REQUEST));
        }

        if (errors.isEmpty()) {
            final UserEntity newUserEntity = new UserEntity(credentials.getLogin(), credentials.getPassword(), credentials.getEmail());
            createUser(newUserEntity);
        }
        return errors;
    }

    @Override
    public Either<UserEntity, List<ErrorResponse>> login(AuthData credentials) {
        final List<ErrorResponse> errors = new ArrayList<>();
        if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getPassword())) {
            errors.add(new ErrorResponse("login and password should be non-empty!", ErrorState.BAD_REQUEST));
        }
        final UserEntity user = getUserByLogin(credentials.getLogin());

        if (user == null) {
            errors.add(new ErrorResponse("User with that login does not exist", ErrorState.FORBIDDEN));
        } else if (!user.getPassword().equals(credentials.getPassword())) {
            errors.add(new ErrorResponse("Incorrect password!", ErrorState.FORBIDDEN));
        }
        if (!errors.isEmpty()) {
            return Either.right(errors);
        }
        //noinspection ConstantConditions
        return Either.left(user); //wont be reached if null
    }

    public UserManager() {
        entityManager = PersistenceManager.getEm();
    }
}
