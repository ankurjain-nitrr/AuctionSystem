package service;

import dao.IAuctionBidDAO;
import dao.IAuctionDAO;
import enums.BidStatus;
import enums.BidStatusResponse;
import exception.DataNotFoundException;
import model.Auction;
import model.AuctionBid;
import model.AuctionFilters;
import service.lock.ILock;
import service.lock.ILockService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Objects;

@Singleton
public class AuctionService {

    private IAuctionDAO auctionDAO;
    private IAuctionBidDAO auctionBidDAO;
    private ILockService lockService;

    @Inject
    public AuctionService(IAuctionDAO auctionDAO, IAuctionBidDAO auctionBidDAO, ILockService lockService) {
        this.auctionDAO = auctionDAO;
        this.auctionBidDAO = auctionBidDAO;
        this.lockService = lockService;
    }

    public Auction getAuction(String itemCode) {
        return auctionDAO.get(itemCode);
    }

    public List<Auction> getAuctions(AuctionFilters auctionFilters, int start, int count) {
        return auctionDAO.get(auctionFilters, start, count);
    }

    public BidStatusResponse bid(AuctionBid auctionBid) throws DataNotFoundException {
        Auction auction = getAuction(auctionBid.getItemCode());
        if (Objects.isNull(auction)) {
            throw new DataNotFoundException("auction not found");
        }
        ILock lock = lockService.getLock(auctionBid.getItemCode());
        BidStatusResponse bidStatusResponse;
        try {
            Objects.requireNonNull(auction);
            bidStatusResponse = getBidStatusResponse(auctionBid, auction);
            if (bidStatusResponse.getBidStatus().equals(BidStatus.ACCEPTED)) {
                auction.setWinningBidPrice(auctionBid.getBidPrice());
                auction.setWinningBidUserId(auctionBid.getUserID());
                auctionDAO.update(auction);
            }
            auctionBid.setBidStatusResponse(bidStatusResponse);
            auctionBidDAO.add(auctionBid);
        } finally {
            lockService.releaseLock(lock);
        }
        return bidStatusResponse;
    }

    private BidStatusResponse getBidStatusResponse(AuctionBid auctionBid, Auction auction) {
        int bidPrice = auctionBid.getBidPrice();
        BidStatusResponse bidStatusResponse = BidStatusResponse.ACCEPTED;
        if (auctionBid.getUserID().equals(auction.getWinningBidUserId())) {
            bidStatusResponse = BidStatusResponse.ALREADY_HIGHEST_BIDDER;
        } else if (bidPrice <= 0) {
            bidStatusResponse = BidStatusResponse.INVALID_VALUE_BID;
        } else if (bidPrice <= auction.getBasePrice()) {
            bidStatusResponse = BidStatusResponse.TOO_LOW;
        } else if (auction.getWinningBidPrice() != null &&
                (bidPrice - auction.getWinningBidPrice()) < auction.getStepRate()) {
            bidStatusResponse = BidStatusResponse.REJECTED_BID;
        } else if ((bidPrice - auction.getBasePrice()) < auction.getStepRate()) {
            bidStatusResponse = BidStatusResponse.REJECTED_BID;
        }
        return bidStatusResponse;
    }
}
