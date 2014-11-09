package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.sun.xml.internal.txw2.Document;

import java.net.UnknownHostException;

/**
 * Created by filippm on 09.11.14..
 */
public class TestMongo {


    public static void main(String[] args) throws UnknownHostException {

        // Since 2.10.0, uses MongoClient
        MongoClient mongo = new MongoClient("localhost", 27017);

        DB db = mongo.getDB("moovis");

        DBCollection table = db.getCollection("users");
        BasicDBObject document = new BasicDBObject();
        document.put("id","1234");
        document.put("name", "caja");
        document.put("age", 22);
        table.insert(document);

        DBCursor cursor = table.find();
        try {
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }

    }
}
