package service;

import dao.IUserDAO;
import exception.AlreadyExistsException;
import exception.DataNotFoundException;
import model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserService {

    @Inject
    private IUserDAO userDAO;

    @Inject
    public UserService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public synchronized void create(User user) throws AlreadyExistsException {
        User exisitingIfAny = userDAO.getByEmail(user.getEmail());
        if (exisitingIfAny == null) {
            userDAO.create(user);
        } else {
            throw new AlreadyExistsException("User with id " + user.getId() + " already exists");
        }
    }

    public synchronized User get(String uid) {
        return userDAO.get(uid);
    }
}
