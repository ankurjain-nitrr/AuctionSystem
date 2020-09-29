package model;

import enums.BidStatusResponse;
import lombok.Getter;

@Getter
public class AuctionBid {

    private String userID;
    private String itemCode;
    private int bidValue;
    private long bidTime;
    private BidStatusResponse bidStatusResponse;


    public AuctionBid(String userID, String itemCode, int bidValue) {
        this.userID = userID;
        this.itemCode = itemCode;
        this.bidValue = bidValue;
        this.bidTime = System.currentTimeMillis();
    }

    public AuctionBid(String userID, String itemCode, int bidValue, BidStatusResponse bidStatusResponse) {
        this(userID, itemCode, bidValue);
        this.bidStatusResponse = bidStatusResponse;
    }
}
