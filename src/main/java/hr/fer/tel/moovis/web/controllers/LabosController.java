package hr.fer.tel.moovis.web.controllers;

import com.mongodb.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.UnknownHostException;

/**
 * Created by filippm on 10.11.14..
 */
@Controller
public class LabosController {
    private static final String DB_NAME = "moovis";

    private MongoClient mongo;
    private DB db;
    private DBCollection movies;

    public LabosController() throws UnknownHostException {
        mongo = new MongoClient("localhost", 27017);
        db = mongo.getDB(DB_NAME);

        movies = db.getCollection("movies");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public String dummy() {
        StringBuilder sb = new StringBuilder();
        DBCursor cursor = movies.find();
        while (cursor.hasNext()) {
            DBObject dbObj = cursor.next();
            sb.append(dbObj.toString()).append("\n");
        }
        return sb.toString();
    }

}
