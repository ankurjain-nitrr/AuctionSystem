package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import enums.AuctionStatus;
import enums.BidStatus;
import enums.BidStatusResponse;
import exception.AlreadyExistsException;
import exception.DataNotFoundException;
import exception.IllegalOperationException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import model.*;
import play.api.http.MimeTypes;
import play.libs.Json;
import play.mvc.*;
import security.Secured;
import service.AuctionService;
import utils.Constants;

import javax.activation.MimeType;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Slf4j
@Api
public class AuctionController extends Controller {

    public static final String QUERY_PARAM_AUCTION_STATUS = "status";

    @Inject

    private AuctionService auctionService;

    @ApiResponses(value = {@ApiResponse(code = 201, message = "Auction created"),
            @ApiResponse(code = 409, message = "Action for item already exists")})
    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "auction details")})
    public Result create(@ApiParam(hidden = true, required = false) Http.Request req) {
        JsonNode jsonNode = req.body().asJson();
        Auction auction = Json.fromJson(jsonNode, Auction.class);
        try {
            auctionService.create(auction);
            return created(Json.toJson(new DisplayAuction(auction))).as(Http.MimeTypes.JSON);
        } catch (AlreadyExistsException e) {
            log.error("Auction already exits for - " + auction.getItemCode());
        }
        return Results.status(Http.Status.CONFLICT, "Already Exists");
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Get paginated list of auctions with next page url")})
    public Result getAuctions(@ApiParam(hidden = true, required = false) Http.Request req, int start, int count) {
        Optional<String> status = Optional.ofNullable(req.getQueryString(QUERY_PARAM_AUCTION_STATUS));
        Optional<AuctionStatus> auctionStatus = status.map(AuctionStatus::valueOf);
        AuctionFilters auctionFilters = AuctionFilters.builder().status(auctionStatus.orElse(null)).build();
        List<Auction> auctions = auctionService.getAuctions(auctionFilters, start, count);
        PaginatedAuctionListResponse paginatedAuctionListResponse =
                PaginatedAuctionListResponse.Builder.from(auctions, start, count, req);
        return ok(paginatedAuctionListResponse.toJSON().toString()).as(Http.MimeTypes.JSON);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "bid price in json")})
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Bid was accepted"),
            @ApiResponse(code = 406, message = "Bid was rejected"),
            @ApiResponse(code = 404, message = "Auction for item not found"),
            @ApiResponse(code = 403, message = "Auction is over")})
    @Security.Authenticated(Secured.class)
    public Result bid(@ApiParam(hidden = true, required = false) Http.Request req, String itemCode) {
        JsonNode requestBody = req.body().asJson();
        Integer bidPrice = requestBody.get("bidPrice").asInt(-1);
        // this wont be null coz authorized
        Optional<String> userId = req.getHeaders().get(Constants.HEADER_KEY_USER_ID);
        AuctionBid auctionBid = new AuctionBid(userId.get(), itemCode, bidPrice);
        BidStatusResponse response = null;
        try {
            response = auctionService.bid(auctionBid);
        } catch (DataNotFoundException e) {
            return notFound("Auction for item not found - " + itemCode);
        } catch (IllegalOperationException e) {
            return forbidden("The auction is over - " + itemCode);
        }
        if (response.getBidStatus().equals(BidStatus.ACCEPTED)) {
            return created(response.getReason());
        } else {
            return notAcceptable(response.getReason());
        }
    }

}
