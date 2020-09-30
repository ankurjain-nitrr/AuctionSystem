package dao.impl;

import com.mongodb.MongoClient;
import dao.IAuctionBidDAO;
import enums.BidStatusResponse;
import model.AuctionBid;
import org.junit.*;
import org.testcontainers.containers.GenericContainer;
import service.MongoDBService;

public class AuctionBidDAOImplTest {

    @ClassRule
    public static GenericContainer<?> mongo =
            new GenericContainer<>("mongo:3.1.5")
                    .withExposedPorts(27017);
    @BeforeClass
    public static void startContainer() {
        mongo.start();
    }

    @Test
    public void addAuctionBidTest() {
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        AuctionBid auctionBid = new AuctionBid("someid", "itemid", 34);
        IAuctionBidDAO auctionBidDAO = new AuctionBidDAOMongoImpl(mongoDBService);
        auctionBidDAO.add(auctionBid);
        AuctionBid latestAuctionBid = auctionBidDAO.getLatest();
        Assert.assertEquals(auctionBid, latestAuctionBid);
    }

    @Test
    public void addAuctionBidTestWithStatus() {
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        AuctionBid auctionBid = new AuctionBid("someid", "itemid", 34);
        auctionBid.setBidStatusResponse(BidStatusResponse.INVALID_VALUE_BID);
        IAuctionBidDAO auctionBidDAO = new AuctionBidDAOMongoImpl(mongoDBService);
        auctionBidDAO.add(auctionBid);
        AuctionBid latestAuctionBid = auctionBidDAO.getLatest();
        Assert.assertEquals(auctionBid, latestAuctionBid);
    }

    @Test
    public void emptyAuctionBidCheck() {
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        IAuctionBidDAO auctionBidDAO = new AuctionBidDAOMongoImpl(mongoDBService);
        AuctionBid latestAuctionBid = auctionBidDAO.getLatest();
        Assert.assertNull(latestAuctionBid);
    }

    @AfterClass
    public static void stopContainer() {
        mongo.stop();
    }
}
