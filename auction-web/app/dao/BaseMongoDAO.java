package dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import service.MongoDBService;

public class BaseMongoDAO {

    MongoDBService mongoDBService;
    private final String dbName;
    private final String colName;

    public BaseMongoDAO(MongoDBService mongoDBService, String dbName, String colName) {
        this.mongoDBService = mongoDBService;
        this.dbName = dbName;
        this.colName = colName;
    }


    protected MongoCollection<Document> getCollection() {
        return mongoDBService.getCollection(dbName, colName);
    }
}