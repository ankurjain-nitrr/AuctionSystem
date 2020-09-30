package model;

import lombok.Getter;

@Getter
public class DisplayAuction {

    private String itemCode;
    private int winningPrice;
    private int stepRate;

    public DisplayAuction(Auction auction) {
        this.itemCode = auction.getItemCode();
        this.winningPrice = auction.getWinningBidPrice();
        this.stepRate = auction.getStepRate();
    }
}
