package dao;

import com.google.inject.ImplementedBy;
import dao.impl.AuctionBidDAOMongoImpl;
import model.AuctionBid;

@ImplementedBy(AuctionBidDAOMongoImpl.class)
public interface IAuctionBidDAO {

    void add(AuctionBid auctionBid);

    AuctionBid getLatest();

    void drop();

}
