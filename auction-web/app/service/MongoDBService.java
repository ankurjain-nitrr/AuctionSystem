package service;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MongoDBService {

    public static final String CONF_KEY_MONGO_HOST = "application.mongo.host";
    public static final String CONF_KEY_MONGO_PORT = "application.mongo.port";


    private MongoClient client;

    @Inject
    public MongoDBService(ConfigurationService configurationService) {
        String mongoHost = configurationService.getString(CONF_KEY_MONGO_HOST);
        int mongoPort = configurationService.getInt(CONF_KEY_MONGO_PORT);
        client = new MongoClient(mongoHost, mongoPort);
    }

    public MongoDBService(MongoClient client) {
        this.client = client;
    }

    public MongoDatabase getDB(String dbName) {
        return client.getDatabase(dbName);
    }

    public MongoCollection<Document> getCollection(String dbName, String colName) {
        return getDB(dbName).getCollection(colName);
    }
}
