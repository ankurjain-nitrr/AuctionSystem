package dao.impl;

import dao.BaseMongoDAO;
import dao.IAuctionBidDAO;
import enums.BidStatusResponse;
import model.AuctionBid;
import model.User;
import org.bson.Document;
import service.MongoDBService;
import utils.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
public class AuctionBidDAOMongoImpl extends BaseMongoDAO implements IAuctionBidDAO {

    public static final String COLLECTION_NAME_AUCTION_BID = "AuctionBid";

    private static final String COL_FIELD_USER_ID = "userId";
    private static final String COL_FIELD_ITEM_CODE = "itemCode";
    private static final String COL_FIELD_BID_PRICE = "bidPrice";
    private static final String COL_FIELD_BID_TIME = "bidTime";
    private static final String COL_FIELD_BID_RESPONSE = "bidStatusResponse";

    @Inject
    public AuctionBidDAOMongoImpl(MongoDBService mongoDBService) {
        super(mongoDBService, Constants.DB_NAME_AUCTION, COLLECTION_NAME_AUCTION_BID);
    }

    @Override
    public void add(AuctionBid auctionBid) {
        getCollection().insertOne(toDocument(auctionBid));
    }

    @Override
    public AuctionBid getLatest() {
        Document latest = getCollection().find().sort(new Document(COL_FIELD_BID_TIME, -1)).first();
        return fromDocument(latest);
    }

    @Nonnull
    private static Document toDocument(@NotNull AuctionBid auctionBid) {
        Document document = new Document().append(COL_FIELD_USER_ID, auctionBid.getUserID())
                .append(COL_FIELD_ITEM_CODE, auctionBid.getItemCode())
                .append(COL_FIELD_BID_PRICE, auctionBid.getBidPrice())
                .append(COL_FIELD_BID_TIME, auctionBid.getBidTime());
        if (auctionBid.getBidStatusResponse() != null) {
            document.append(COL_FIELD_BID_RESPONSE, auctionBid.getBidStatusResponse().name());
        }
        return document;
    }

    @Nullable
    private static AuctionBid fromDocument(Document auctionBidDocument) {
        AuctionBid auctionBid = null;
        if (auctionBidDocument != null) {
            BidStatusResponse statusResponse = null;
            if (auctionBidDocument.containsKey(COL_FIELD_BID_RESPONSE)) {
                statusResponse = BidStatusResponse.valueOf(auctionBidDocument.getString(COL_FIELD_BID_RESPONSE));
            }
            auctionBid = new AuctionBid(auctionBidDocument.getString(COL_FIELD_USER_ID),
                    auctionBidDocument.getString(COL_FIELD_ITEM_CODE),
                    auctionBidDocument.getInteger(COL_FIELD_BID_PRICE),
                    auctionBidDocument.getLong(COL_FIELD_BID_TIME), statusResponse);
        }
        return auctionBid;
    }

    @Override
    public void drop() {
        getCollection().drop();
    }
}
