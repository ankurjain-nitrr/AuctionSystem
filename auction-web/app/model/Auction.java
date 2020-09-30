package model;

import enums.AuctionStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class Auction {

    private String itemCode;
    private int basePrice;
    private int stepRate;
    @Setter
    private AuctionStatus auctionStatus;
    @Setter
    private Integer winningBidPrice;
    @Setter
    private String winningBidUserId;
    private long created;

    public Auction() {
        this.auctionStatus = AuctionStatus.RUNNING;
        this.created = System.currentTimeMillis();
    }

    public Auction(String itemCode, int basePrice, int stepRate) {
        this.itemCode = itemCode;
        this.basePrice = basePrice;
        this.stepRate = stepRate;
        this.auctionStatus = AuctionStatus.RUNNING;
        this.created = System.currentTimeMillis();
    }

    public Auction(String itemCode, int basePrice, int stepRate, AuctionStatus auctionStatus,
                   Integer winningBidPrice, String winningBidUserId, long created) {
        this.itemCode = itemCode;
        this.basePrice = basePrice;
        this.stepRate = stepRate;
        this.auctionStatus = auctionStatus;
        this.winningBidPrice = winningBidPrice;
        this.winningBidUserId = winningBidUserId;
        this.created = created;
    }
}
