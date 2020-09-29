package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BidStatusResponse {

    ACCEPTED(BidStatus.ACCEPTED, "Valid Bid"),
    INVALID_VALUE_BID(BidStatus.REJECTED, "Bid value is not acceptable"),
    TOO_LOW(BidStatus.REJECTED, "Bid value is lower than base price"),
    REJECTED_BID(BidStatus.REJECTED, "Bid value doesnt beat the previous bit by acceptable margin");

    private BidStatus bidStatus;
    private String reason;

}
