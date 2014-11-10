package hr.fer.tel.moovis.searchers;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

/**
 * Created by filippm on 09.11.14..
 */
public class InitYTQueue {


    public static void main(String[] args) throws UnknownHostException {

        // Since 2.10.0, uses MongoClient
        MongoClient mongo = new MongoClient("localhost", 27017);

        DB db = mongo.getDB("moovis");

        DBCollection table = db.getCollection("YTSearchQueue");
        BasicDBObject document = new BasicDBObject();
        document.put("movieKey", "Django Unchained2012");
        document.put("name", "Django Unchained");
        document.put("year", "2012");

        table.insert(document);

        document = new BasicDBObject();
        document.put("movieKey", "Django Unchained2012");
        document.put("name", "Django Unchained");
        document.put("year", "2012");

        table.insert(document);

        document = new BasicDBObject();
        document.put("movieKey", "Django Unchained2012");
        document.put("name", "Django Unchained");
        document.put("year", "2012");

        table.insert(document);


    }
}
