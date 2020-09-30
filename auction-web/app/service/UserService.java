package service;

import dao.IAuthenticationTokenDAO;
import dao.IUserDAO;
import exception.AlreadyExistsException;
import model.AuthenticationToken;
import model.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private static final int EXPIRY_AUTHENTICATION_TOKEN_IN_DAYS = 30;

    private IUserDAO userDAO;
    private IAuthenticationTokenDAO authenticationTokenDAO;

    @Inject
    public UserService(IUserDAO userDAO, IAuthenticationTokenDAO authenticationTokenDAO) {
        this.userDAO = userDAO;
        this.authenticationTokenDAO = authenticationTokenDAO;
    }

    public synchronized void create(User user) throws AlreadyExistsException {
        userDAO.create(user);
    }

    public Optional<AuthenticationToken> authenticateAndGenerateToken(String email, String passwdmd5) {
        Optional<AuthenticationToken> generatedToken = Optional.empty();
        User user = userDAO.getByEmail(email);
        if (user != null && user.getPasswordMD5().equals(passwdmd5)) {
            AuthenticationToken authenticationToken =
                    new AuthenticationToken(user.getId(), generateToken(), EXPIRY_AUTHENTICATION_TOKEN_IN_DAYS);
            authenticationTokenDAO.create(authenticationToken);
            generatedToken = Optional.of(authenticationToken);
        }
        return generatedToken;
    }

    public Optional<User> authenticateToken(String token, String userId) {
        User user = null;
        AuthenticationToken authenticationToken = authenticationTokenDAO.get(token);
        if (authenticationToken.validate(userId)) {
            user = userDAO.get(userId);
        }
        return Optional.ofNullable(user);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public User get(String uid) {
        return userDAO.get(uid);
    }
}
