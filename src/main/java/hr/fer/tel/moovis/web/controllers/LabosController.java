package hr.fer.tel.moovis.web.controllers;

import com.mongodb.*;
import hr.fer.tel.moovis.apis.TwitterAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import twitter4j.PagableResponseList;
import twitter4j.User;

import javax.xml.ws.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "/list/{limit}", method = RequestMethod.GET)
    public ResponseEntity<List<DBObject>> dummy(@PathVariable Integer limit) {
        List<DBObject> retList = new ArrayList<DBObject>();
        DBCursor cursor = movies.find();
        int counter = 0;
        while (cursor.hasNext()) {
            if (counter > limit) {
                break;
            }
            counter++;
            DBObject dbObj = cursor.next();
            retList.add(dbObj);
        }
        return new ResponseEntity<List<DBObject>>(retList, HttpStatus.OK);
    }

    @RequestMapping(value = "/twitter", method = RequestMethod.GET)
    public ResponseEntity<List<String>> twitterUsers() {

        TwitterAPI twitterApi = new TwitterAPI();
        List<String> users = twitterApi.getFollowedUsers();

        if (users != null) return new ResponseEntity<List<String>>(users, HttpStatus.OK);

        return null;
    }
}
