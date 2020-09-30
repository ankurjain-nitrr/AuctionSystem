package service;

import com.mongodb.MongoClient;
import dao.IAuctionBidDAO;
import dao.IAuctionDAO;
import dao.impl.AuctionBidDAOMongoImpl;
import dao.impl.AuctionDAOMongoImpl;
import enums.BidStatusResponse;
import exception.DataNotFoundException;
import model.Auction;
import model.AuctionBid;
import org.junit.*;
import org.testcontainers.containers.GenericContainer;
import service.lock.ILockService;
import service.lock.impl.LockServiceStripedImpl;

public class AuctionServiceTest {

    @ClassRule
    public static GenericContainer<?> mongo =
            new GenericContainer<>("mongo:3.1.5")
                    .withExposedPorts(27017);

    private static AuctionService auctionService;
    private static IAuctionDAO auctionDAO;
    private static IAuctionBidDAO auctionBidDAO;

    @BeforeClass
    public static void startContainer() {
        mongo.start();
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        auctionDAO = new AuctionDAOMongoImpl(mongoDBService);
        auctionBidDAO = new AuctionBidDAOMongoImpl(mongoDBService);
        ILockService lockService = new LockServiceStripedImpl();
        auctionService = new AuctionService(auctionDAO, auctionBidDAO, lockService);
    }

    @Before
    public void clearBiddings() {
        auctionBidDAO.drop();
        auctionDAO.drop();
        auctionDAO.create(new Auction("item", 30, 5));
    }

    @Test
    public void testBidResponseInvalidBid() throws DataNotFoundException {
        BidStatusResponse response =
                auctionService.bid(new AuctionBid("user1", "item", -1));
        Assert.assertEquals(BidStatusResponse.INVALID_VALUE_BID, response);
    }

    @Test(expected = DataNotFoundException.class)
    public void testBidResponseInvalidAuction() throws DataNotFoundException {
        BidStatusResponse response =
                auctionService.bid(new AuctionBid("user1", "ite", 40));
    }

    @Test()
    public void testBidResponseTooLowBid() throws DataNotFoundException {
        BidStatusResponse response =
                auctionService.bid(new AuctionBid("user1", "item", 1));
        Assert.assertEquals(BidStatusResponse.TOO_LOW, response);
    }

    @Test()
    public void testBidResponseStepRate() throws DataNotFoundException {
        BidStatusResponse response =
                auctionService.bid(new AuctionBid("user1", "item", 32));
        Assert.assertEquals(BidStatusResponse.REJECTED_BID, response);
    }

    @Test()
    public void testBidResponseValidBid() throws DataNotFoundException {
        BidStatusResponse response =
                auctionService.bid(new AuctionBid("user1", "item", 35));
        Assert.assertEquals(BidStatusResponse.ACCEPTED, response);
    }

    @Test()
    public void testBidResponseRebid() throws DataNotFoundException {
        BidStatusResponse responseFirst =
                auctionService.bid(new AuctionBid("user1", "item", 35));
        BidStatusResponse responseSecond = auctionService.bid(new AuctionBid("user1", "item", 40));
        Assert.assertEquals(BidStatusResponse.ALREADY_HIGHEST_BIDDER, responseSecond);
    }

    @Test()
    public void testBidResponseBeatingBid() throws DataNotFoundException {
        BidStatusResponse responseFirst =
                auctionService.bid(new AuctionBid("user1", "item", 35));
        BidStatusResponse responseSecond = auctionService.bid(new AuctionBid("user2", "item", 40));
        Assert.assertEquals(BidStatusResponse.ACCEPTED, responseSecond);
    }

    @Test()
    public void testBidResponseBeatedByHighestBid() throws DataNotFoundException {
        BidStatusResponse responseFirst =
                auctionService.bid(new AuctionBid("user1", "item", 35));
        BidStatusResponse responseSecond = auctionService.bid(new AuctionBid("user2", "item", 37));
        Assert.assertEquals(BidStatusResponse.REJECTED_BID, responseSecond);
    }

    @AfterClass
    public static void stopContainer() {
        mongo.stop();
    }
}