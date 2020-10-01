package dao;

import com.google.inject.ImplementedBy;
import dao.impl.UserDAOMongoImpl;
import exception.AlreadyExistsException;
import exception.DataNotFoundException;
import model.User;

@ImplementedBy(UserDAOMongoImpl.class)
public interface IUserDAO {

    void create(User user) throws AlreadyExistsException;

    User get(String userID);

    User getByEmail(String email);

    void update(User user) throws DataNotFoundException;

    void delete(String userID) throws DataNotFoundException;

    void drop();
}
