package model;

import enums.AuctionStatus;
import lombok.Getter;

@Getter
public class Auction {

    private String itemCode;
    private int basePrice;
    private int stepRate;
    private AuctionStatus auctionStatus;
    private Integer winningBidPrice;
    private long created;

    public Auction(String itemCode, int basePrice, int stepRate) {
        this.itemCode = itemCode;
        this.basePrice = basePrice;
        this.stepRate = stepRate;
        this.auctionStatus = AuctionStatus.RUNNING;
        this.created = System.currentTimeMillis();
    }
}
