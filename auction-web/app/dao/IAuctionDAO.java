package dao;

import com.google.inject.ImplementedBy;
import dao.impl.AuctionDAOMongoImpl;
import enums.AuctionStatus;
import exception.AlreadyExistsException;
import model.Auction;
import model.AuctionFilters;

import java.util.List;

@ImplementedBy(AuctionDAOMongoImpl.class)
public interface IAuctionDAO {

    void create(Auction auction) throws AlreadyExistsException;

    List<Auction> get(AuctionFilters auctionFilters, int start, int count);

    Auction get(String itemCode);

    void update(Auction auction);

    void drop();

}
