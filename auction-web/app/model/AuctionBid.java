package model;

import enums.BidStatusResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
public class AuctionBid {

    private String userID;
    private String itemCode;
    private int bidPrice;
    private long bidTime;
    @Setter
    private BidStatusResponse bidStatusResponse;


    public AuctionBid(String userID, String itemCode, int bidPrice) {
        this.userID = userID;
        this.itemCode = itemCode;
        this.bidPrice = bidPrice;
        this.bidTime = System.currentTimeMillis();
    }

    public AuctionBid(String userID, String itemCode, int bidPrice, long bidTime, BidStatusResponse bidStatusResponse) {
        this.userID = userID;
        this.itemCode = itemCode;
        this.bidPrice = bidPrice;
        this.bidTime = bidTime;
        this.bidStatusResponse = bidStatusResponse;
    }
}
