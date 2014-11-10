package hr.fer.tel.moovis.searchers;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

/**
 * Created by Damjan on 10.11.2014..
 */
public class InitRottenTomatoesQueue {

    public static void main(String[] args) throws UnknownHostException {

        // Since 2.10.0, uses MongoClient
        MongoClient mongo = new MongoClient("localhost", 27017);

        DB db = mongo.getDB("moovis");

        DBCollection table = db.getCollection("RottenSearchQueue");
        BasicDBObject document = new BasicDBObject();
        document.put("movieKey", "Django Unchained1");
        document.put("name", "Django Unchained");
        document.put("year", "2012");

        table.insert(document);

        document = new BasicDBObject();
        document.put("movieKey", "Gone Girl");
        document.put("name", "Gone Girl");
        document.put("year", "2014");

        table.insert(document);




    }
}
