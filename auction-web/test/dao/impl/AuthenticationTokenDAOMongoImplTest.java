package dao.impl;

import com.mongodb.MongoClient;
import dao.IAuthenticationTokenDAO;
import model.AuthenticationToken;
import org.junit.*;
import org.testcontainers.containers.GenericContainer;
import service.MongoDBService;

public class AuthenticationTokenDAOMongoImplTest {

    @ClassRule
    public static GenericContainer<?> mongo =
            new GenericContainer<>("mongo:3.1.5")
                    .withExposedPorts(27017);

    @BeforeClass
    public static void startContainer() {
        mongo.start();
    }

    @Test
    public void addToken() {
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IAuthenticationTokenDAO authenticationTokenDAO = new AuthenticationTokenDAOMongoImpl(mongoDBService);
        AuthenticationToken authenticationToken = new AuthenticationToken("userid", "tokenid", 30);
        authenticationTokenDAO.create(authenticationToken);
        AuthenticationToken fetchedToken = authenticationTokenDAO.get(authenticationToken.getToken());
        Assert.assertEquals(fetchedToken, authenticationToken);
    }


    @Test
    public void invalidToken() {
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IAuthenticationTokenDAO authenticationTokenDAO = new AuthenticationTokenDAOMongoImpl(mongoDBService);
        AuthenticationToken fetchedToken = authenticationTokenDAO.get("tokenNotAvailable");
        Assert.assertNull(fetchedToken);
    }


    @AfterClass
    public static void stopContainer() {
        mongo.stop();
    }

}
