package model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class DisplayAuction implements Serializable {

    private String itemCode;
    private int winningPrice;
    private int stepRate;

    public DisplayAuction(Auction auction) {
        this.itemCode = auction.getItemCode();
        this.winningPrice = auction.getWinningBidPrice() == null ?
                auction.getBasePrice() : auction.getWinningBidPrice();
        this.stepRate = auction.getStepRate();
    }
}
