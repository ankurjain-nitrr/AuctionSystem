package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum BidStatusResponse {

    ACCEPTED(BidStatus.ACCEPTED, "Valid Bid"),
    INVALID_VALUE_BID(BidStatus.REJECTED, "Bid value is not acceptable"),
    TOO_LOW(BidStatus.REJECTED, "Bid value is lower than base price"),
    REJECTED_BID(BidStatus.REJECTED, "Bid value doesnt beat the previous bit by acceptable margin"),
    ALREADY_HIGHEST_BIDDER(BidStatus.REJECTED, "User is already the highest bidder");

    private BidStatus bidStatus;
    private String reason;

    BidStatusResponse(BidStatus bidStatus, String reason) {
        this.bidStatus = bidStatus;
        this.reason = reason;
    }
}
