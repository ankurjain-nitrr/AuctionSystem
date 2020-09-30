package dao.impl;

import com.mongodb.client.FindIterable;
import dao.BaseMongoDAO;
import dao.IAuctionDAO;
import enums.AuctionStatus;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.Auction;
import model.AuctionFilters;
import model.User;
import org.bson.Document;
import service.MongoDBService;
import utils.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Singleton
public class AuctionDAOMongoImpl extends BaseMongoDAO implements IAuctionDAO {

    public static final String COLLECTION_NAME_AUCTION = "Auction";

    private static final String COL_FIELD_ITEM_CODE = "itemCode";
    private static final String COL_FIELD_BASE_PRICE = "basePrice";
    private static final String COL_FIELD_STEP_RATE = "stepRate";
    private static final String COL_FIELD_AUCTION_STATUS= "auctionStatus";
    private static final String COL_FIELD_WINNING_BID_PRICE = "winningBidPrice";
    private static final String COL_FIELD_WINNING_BID_USER_ID = "winningBidUserId";
    private static final String COL_FIELD_CREATED = "c";

    @Inject
    public AuctionDAOMongoImpl(MongoDBService mongoDBService) {
        super(mongoDBService, Constants.DB_NAME_AUCTION, COLLECTION_NAME_AUCTION);
    }

    @Override
    public void create(Auction auction) {
        getCollection().insertOne(toDocument(auction));
    }

    @Override
    public List<Auction> get(AuctionFilters auctionFilters, int start, int count) {
        Document filter = new Document();
        if (Objects.nonNull(auctionFilters.getStatus())) {
            log.debug("will filter auctions based on status - " + auctionFilters.getStatus());
            filter.append(COL_FIELD_AUCTION_STATUS, auctionFilters.getStatus().name());
        } else {
            log.debug("will return all auctions");
        }
        List<Auction> result = new ArrayList<>();
        getCollection().find(filter).skip(start).limit(count).forEach((Consumer<? super Document>) document -> {
            result.add(fromDocument(document));
        });
        return result;
    }

    @Override
    public Auction get(String itemCode) {
        Document filter = new Document().append(COL_FIELD_ITEM_CODE, itemCode);
        return fromDocument(getCollection().find(filter).first());
    }

    @Override
    public void update(Auction auction) {
        Document filter = new Document().append(COL_FIELD_ITEM_CODE, auction.getItemCode());
        getCollection().replaceOne(filter, toDocument(auction));
    }

    @Override
    public void drop() {
        getCollection().drop();
    }

    @Nonnull
    private static Document toDocument(@NotNull Auction auction) {
        return new Document().append(COL_FIELD_ITEM_CODE, auction.getItemCode())
                .append(COL_FIELD_BASE_PRICE, auction.getBasePrice())
                .append(COL_FIELD_STEP_RATE, auction.getStepRate())
                .append(COL_FIELD_AUCTION_STATUS, auction.getAuctionStatus().name())
                .append(COL_FIELD_WINNING_BID_PRICE, auction.getWinningBidPrice())
                .append(COL_FIELD_WINNING_BID_USER_ID, auction.getWinningBidUserId())
                .append(COL_FIELD_CREATED, auction.getCreated());
    }

    @Nullable
    private static Auction fromDocument(Document auctionDocument) {
        Auction auction = null;
        if (auctionDocument != null) {
            auction = new Auction(auctionDocument.getString(COL_FIELD_ITEM_CODE),
                    auctionDocument.getInteger(COL_FIELD_BASE_PRICE),
                    auctionDocument.getInteger(COL_FIELD_STEP_RATE),
                    AuctionStatus.valueOf(auctionDocument.getString(COL_FIELD_AUCTION_STATUS)),
                    auctionDocument.getInteger(COL_FIELD_WINNING_BID_PRICE),
                    auctionDocument.getString(COL_FIELD_WINNING_BID_USER_ID),
                    auctionDocument.getLong(COL_FIELD_CREATED));
        }
        return auction;
    }

}