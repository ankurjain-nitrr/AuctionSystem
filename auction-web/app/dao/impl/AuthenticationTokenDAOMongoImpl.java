package dao.impl;

import dao.BaseMongoDAO;
import dao.IAuthenticationTokenDAO;
import enums.AuctionStatus;
import model.Auction;
import model.AuthenticationToken;
import org.bson.Document;
import service.MongoDBService;
import utils.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
public class AuthenticationTokenDAOMongoImpl extends BaseMongoDAO implements IAuthenticationTokenDAO {

    public static final String COLLECTION_NAME_AUTHENTICATION_TOKEN = "AuthenticationToken";

    private static final String COL_FIELD_USER_ID = "userId";
    private static final String COL_FIELD_TOKEN = "token";
    private static final String COL_FIELD_CREATED = "created";
    private static final String COL_FIELD_EXPIRY = "expiry";

    @Inject
    public AuthenticationTokenDAOMongoImpl(MongoDBService mongoDBService) {
        super(mongoDBService, Constants.DB_NAME_AUCTION, COLLECTION_NAME_AUTHENTICATION_TOKEN);
    }

    @Override
    public AuthenticationToken get(String token) {
        Document tokenDocument = getCollection().find(new Document().append(COL_FIELD_TOKEN, token)).first();
        return fromDocument(tokenDocument);
    }

    @Override
    public void create(AuthenticationToken authenticationToken) {
        getCollection().insertOne(toDocument(authenticationToken));
    }

    @Nonnull
    private static Document toDocument(@NotNull AuthenticationToken authenticationToken) {
        return new Document().append(COL_FIELD_USER_ID, authenticationToken.getUserId())
                .append(COL_FIELD_TOKEN, authenticationToken.getToken())
                .append(COL_FIELD_CREATED, authenticationToken.getCreated())
                .append(COL_FIELD_EXPIRY, authenticationToken.getExpiry());
    }

    @Nullable
    private static AuthenticationToken fromDocument(Document tokenDocument) {
        AuthenticationToken authenticationToken = null;
        if (tokenDocument != null) {
            authenticationToken = new AuthenticationToken(tokenDocument.getString(COL_FIELD_USER_ID),
                    tokenDocument.getString(COL_FIELD_TOKEN),
                    tokenDocument.getLong(COL_FIELD_CREATED),
                    tokenDocument.getLong(COL_FIELD_EXPIRY));
        }
        return authenticationToken;
    }
}
