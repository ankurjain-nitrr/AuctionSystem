package model;

import enums.AuctionStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionFilters {

    private AuctionStatus status;

}
