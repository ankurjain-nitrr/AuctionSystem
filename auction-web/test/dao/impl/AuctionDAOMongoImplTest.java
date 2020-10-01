package dao.impl;

import com.mongodb.MongoClient;
import dao.IAuctionDAO;
import enums.AuctionStatus;
import exception.AlreadyExistsException;
import model.Auction;
import model.AuctionFilters;
import org.jetbrains.annotations.NotNull;
import org.junit.*;
import org.testcontainers.containers.GenericContainer;
import service.MongoDBService;

import java.util.List;

public class AuctionDAOMongoImplTest {

    @ClassRule
    public static GenericContainer<?> mongo =
            new GenericContainer<>("mongo:3.1.5")
                    .withExposedPorts(27017);

    @BeforeClass
    public static void startContainer() {
        mongo.start();
    }


    @NotNull
    private IAuctionDAO getAuctionDAO() {
        MongoClient mongoClient = new MongoClient(mongo.getHost(), mongo.getMappedPort(27017));
        MongoDBService mongoDBService = new MongoDBService(mongoClient);
        return new AuctionDAOMongoImpl(mongoDBService);
    }

    @Test
    public void nonAuction() {
        IAuctionDAO auctionDAO = getAuctionDAO();
        Assert.assertNull(auctionDAO.get("item"));
    }

    @Test
    public void addAuction() throws AlreadyExistsException {
        IAuctionDAO auctionDAO = getAuctionDAO();
        Auction auction = new Auction("item", 50, 5);
        auctionDAO.create(auction);
        Auction fetchedAuction = auctionDAO.get(auction.getItemCode());
        Assert.assertEquals(fetchedAuction, auction);
    }

    @Test
    public void updateAuction() throws AlreadyExistsException {
        IAuctionDAO auctionDAO = getAuctionDAO();
        auctionDAO.drop();
        Auction auction = new Auction("item", 50, 5);
        auctionDAO.create(auction);
        auction.setAuctionStatus(AuctionStatus.OVER);
        auctionDAO.update(auction);
        Auction fetchedAuction = auctionDAO.get(auction.getItemCode());
        Assert.assertEquals(fetchedAuction, auction);
    }

    @Test
    public void fetchRunningAuctionsEmpty() {
        IAuctionDAO auctionDAO = getAuctionDAO();
        auctionDAO.drop();
        List<Auction> auctions =
                auctionDAO.get(AuctionFilters.builder().status(AuctionStatus.RUNNING).build(), 0, 4);
        Assert.assertEquals(auctions.size(), 0);
    }

    @Test
    public void fetchRunningAuctionsFilter() throws AlreadyExistsException {
        IAuctionDAO auctionDAO = getAuctionDAO();
        auctionDAO.drop();
        Auction auction = new Auction("item", 50, 5);
        auction.setAuctionStatus(AuctionStatus.OVER);
        auctionDAO.create(auction);
        Auction auctionTwo = new Auction("item1", 50, 5);
        auctionDAO.create(auctionTwo);
        List<Auction> auctions =
                auctionDAO.get(AuctionFilters.builder().status(AuctionStatus.RUNNING).build(), 0, 4);
        Assert.assertEquals(auctions.size(), 1);
    }

    @Test
    public void fetchRunningAuctionsPagination() throws AlreadyExistsException {
        IAuctionDAO auctionDAO = getAuctionDAO();
        auctionDAO.drop();
        for(int i = 0; i < 7; i ++) {
            Auction auction = new Auction("item" + i, 50, 5);
            auctionDAO.create(auction);
        }

        List<Auction> auctions =
                auctionDAO.get(AuctionFilters.builder().status(AuctionStatus.RUNNING).build(), 4, 4);
        Assert.assertEquals(auctions.size(), 3);
    }

    @AfterClass
    public static void stopContainer() {
        mongo.stop();
    }
}
