package dao.impl;

import com.mongodb.MongoClient;
import dao.IUserDAO;
import exception.DataNotFoundException;
import model.User;
import org.junit.*;
import org.testcontainers.containers.GenericContainer;
import service.MongoDBService;

public class UserDAOMongoImplTest {


    @BeforeClass
    public static void startContainer() {
        mongo.start();
    }

    @ClassRule
    public static GenericContainer<?> mongo =
            new GenericContainer<>("mongo:3.1.5")
                    .withExposedPorts(27017);

    @Test
    public void addUserTest() {
        User createdUser = new User("Ankur", "ankur@google.com", "!@#$%^&");
        String createdUserID = createdUser.getId();
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IUserDAO userDAO = new UserDAOMongoImpl(mongoDBService);
        userDAO.create(createdUser);
        User retrievedUser = userDAO.get(createdUserID);
        Assert.assertEquals(createdUser, retrievedUser);
    }

    @Test(expected = DataNotFoundException.class)
    public void deleteNonExistentUserTest() throws DataNotFoundException {
        User createdUser = new User("Ankur", "ankur@google.com", "!@#$%^&");
        String createdUserID = createdUser.getId();
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IUserDAO userDAO = new UserDAOMongoImpl(mongoDBService);
        userDAO.delete(createdUserID);
    }

    @Test
    public void deleteExistingUserTest() throws DataNotFoundException {
        User createdUser = new User("Ankur", "ankur@google.com", "!@#$%^&");
        String createdUserID = createdUser.getId();
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IUserDAO userDAO = new UserDAOMongoImpl(mongoDBService);
        userDAO.create(createdUser);
        userDAO.delete(createdUserID);
        User retrievedUser = userDAO.get(createdUserID);
        Assert.assertNull(retrievedUser);
    }

    @Test(expected = DataNotFoundException.class)
    public void updateNonExistingUserTest() throws DataNotFoundException {
        User createdUser = new User("Ankur", "ankur@google.com", "!@#$%^&");
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IUserDAO userDAO = new UserDAOMongoImpl(mongoDBService);
        createdUser.setName("Ankit");
        userDAO.update(createdUser);
    }

    @Test
    public void updateExistingUserTest() throws DataNotFoundException {
        User createdUser = new User("Ankur", "ankur@google.com", "!@#$%^&");
        String createdUserID = createdUser.getId();
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IUserDAO userDAO = new UserDAOMongoImpl(mongoDBService);
        userDAO.create(createdUser);
        createdUser.setName("Ankit");
        userDAO.update(createdUser);
        User updatedUser = userDAO.get(createdUserID);
        Assert.assertEquals(createdUser, updatedUser);
    }


    @AfterClass
    public static void stopContainer() {
        mongo.stop();
    }
}