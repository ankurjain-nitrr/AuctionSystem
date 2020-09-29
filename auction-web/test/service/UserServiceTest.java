package service;

import com.mongodb.MongoClient;
import dao.IUserDAO;
import dao.impl.UserDAOMongoImpl;
import exception.AlreadyExistsException;
import model.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

public class UserServiceTest {

    @BeforeClass
    public static void startContainer() {
        mongo.start();
    }

    @ClassRule
    public static GenericContainer<?> mongo =
            new GenericContainer<>("mongo:3.1.5")
                    .withExposedPorts(27017);

    @Test(expected = AlreadyExistsException.class)
    public void duplicateUserTest() throws AlreadyExistsException {
        User createdUser = new User("Ankur", "ankur@google.com", "!@#$%^&");
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IUserDAO userDAO = new UserDAOMongoImpl(mongoDBService);
        UserService userService = new UserService(userDAO);
        userService.create(createdUser);
        userService.create(createdUser);
    }

}
