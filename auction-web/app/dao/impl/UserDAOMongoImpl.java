package dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dao.BaseMongoDAO;
import dao.IUserDAO;
import exception.AlreadyExistsException;
import exception.DataNotFoundException;
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
public class UserDAOMongoImpl extends BaseMongoDAO implements IUserDAO {

    public static final String COLLECTION_NAME_USER = "User";

    private static final String COL_FIELD_ID = "uid";
    private static final String COL_FIELD_NAME = "name";
    private static final String COL_FIELD_EMAIL = "email";
    private static final String COL_FIELD_PASSWORD_MD5 = "psswdmd";
    private static final String COL_FIELD_CREATED = "c";

    @Inject
    public UserDAOMongoImpl(MongoDBService mongoDBService) {
        super(mongoDBService, Constants.DB_NAME_AUCTION, COLLECTION_NAME_USER);
        getCollection().createIndex(new Document(COL_FIELD_ID, 1), new IndexOptions().unique(true));
        getCollection().createIndex(new Document(COL_FIELD_EMAIL, 1), new IndexOptions().unique(true));
    }

    @Override
    public void create(@Nonnull User user) throws AlreadyExistsException {
        try {
            getCollection().insertOne(toDocument(user));
        } catch (MongoWriteException ex) {
            throw new AlreadyExistsException(("User with email - " + user.getEmail() + " already present"), ex);
        }
    }

    @Override
    @Nullable
    public User get(String userID) {
        Document userDocument =
                getCollection().find(new Document(COL_FIELD_ID, userID)).first();
        return fromDocument(userDocument);
    }

    @Override
    public User getByEmail(String email) {
        Document userDocument =
                getCollection().find(new Document(COL_FIELD_EMAIL, email)).first();
        return fromDocument(userDocument);
    }

    @Override
    public void update(User user) throws DataNotFoundException {
        BasicDBObject filterBson = new BasicDBObject();
        filterBson.put(COL_FIELD_ID, user.getId());
        UpdateResult updateResult =
                getCollection().replaceOne(filterBson, toDocument(user));
        if (updateResult.getModifiedCount() < 1) {
            throw new DataNotFoundException("User to be updated not found - " + user.getId());
        }
    }

    @Override
    public void delete(String userID) throws DataNotFoundException {
        DeleteResult deleteResult = getCollection().deleteOne(new Document(COL_FIELD_ID, userID));
        long deletedCount = deleteResult.getDeletedCount();
        if (deletedCount < 1) {
            throw new DataNotFoundException("User with id - " + userID + "not found");
        }
    }

    @Override
    public void drop() {
        getCollection().drop();
    }

    @Nonnull
    private static Document toDocument(@NotNull User user) {
        return new Document().append(COL_FIELD_ID, user.getId()).append(COL_FIELD_NAME, user.getName())
                .append(COL_FIELD_EMAIL, user.getEmail()).append(COL_FIELD_PASSWORD_MD5, user.getPasswd())
                .append(COL_FIELD_CREATED, user.getCreated());
    }

    @Nullable
    private static User fromDocument(Document userDocument) {
        User user = null;
        if (userDocument != null) {
            user = new User(userDocument.getString(COL_FIELD_ID), userDocument.getString(COL_FIELD_NAME),
                    userDocument.getString(COL_FIELD_EMAIL), userDocument.getString(COL_FIELD_PASSWORD_MD5),
                    userDocument.getLong(COL_FIELD_CREATED));
        }
        return user;
    }
}
