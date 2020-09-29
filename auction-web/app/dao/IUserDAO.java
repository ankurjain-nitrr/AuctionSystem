package dao;

import exception.DataNotFoundException;
import model.User;

public interface IUserDAO {

    void create(User user);

    User get(String userID);

    User getByEmail(String email);

    void update(User user) throws DataNotFoundException;

    void delete(String userID) throws DataNotFoundException;

}
