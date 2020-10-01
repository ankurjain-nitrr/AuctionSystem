package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import enums.AuctionStatus;
import enums.BidStatus;
import enums.BidStatusResponse;
import exception.AlreadyExistsException;
import exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import model.*;
import play.libs.Json;
import play.mvc.*;
import security.Secured;
import service.AuctionService;
import utils.Constants;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class AuctionController extends Controller {

    public static final String QUERY_PARAM_AUCTION_STATUS = "status";

    @Inject
    private AuctionService auctionService;

    public Result create(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        Auction auction = Json.fromJson(jsonNode, Auction.class);
        try {
            auctionService.create(auction);
            return created(Json.toJson(new DisplayAuction(auction)));
        } catch (AlreadyExistsException e) {
            log.error("Auction already exits for - " + auction.getItemCode());
        }
        return Results.status(Http.Status.CONFLICT, "Already Exists");
    }

    public Result getAuctions(Http.Request request, int start, int count) {
        Optional<String> status = Optional.ofNullable(request.getQueryString(QUERY_PARAM_AUCTION_STATUS));
        Optional<AuctionStatus> auctionStatus = status.map(AuctionStatus::valueOf);
        AuctionFilters auctionFilters = AuctionFilters.builder().status(auctionStatus.orElse(null)).build();
        List<Auction> auctions = auctionService.getAuctions(auctionFilters, start, count);
        String currentPageURI = request.uri();
        PaginatedAuctionListResponse paginatedAuctionListResponse =
                PaginatedAuctionListResponse.Builder.from(auctions, start, count, currentPageURI);
        return ok(Json.toJson(paginatedAuctionListResponse));
    }

    @Security.Authenticated(Secured.class)
    public Result bid(Http.Request request, String itemCode) {
        JsonNode requestBody = request.body().asJson();
        Integer bidPrice = requestBody.get("bidPrice").asInt(-1);
        // this wont be null coz authorized
        Optional<String> userId = request.getHeaders().get(Constants.HEADER_KEY_USER_ID);
        AuctionBid auctionBid = new AuctionBid(userId.get(), itemCode, bidPrice);
        BidStatusResponse response = null;
        try {
            response = auctionService.bid(auctionBid);
        } catch (DataNotFoundException e) {
            return notFound("Auction for item not found - " + itemCode);
        }
        if (response.getBidStatus().equals(BidStatus.ACCEPTED)) {
            return created(response.getReason());
        } else {
            return notAcceptable(response.getReason());
        }
    }

}
