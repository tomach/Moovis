package hr.fer.tel.moovis.dao;

import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import hr.fer.tel.moovis.model.ApplicationUser;

public class ApplicationUserDao {

	private DBCollection applicationUsersCollection;

	public ApplicationUserDao() throws UnknownHostException {
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("moovis");
		applicationUsersCollection = db.getCollection("ApplicationUsers");
	}

	public void save(String name, String surname, String fbAccessToken,
			String fbId, List<String> friendIds) {
		// check if user exists
		DBObject finder = new BasicDBObject("fb_id", fbId);
		if (applicationUsersCollection.findOne(finder) != null) {
			BasicDBObject updater = new BasicDBObject();
			updater.append("name", name);
			updater.append("surname", surname);
			updater.append("fb_access_token", fbAccessToken);
			updater.append("fb_id", fbId);
			updater.append("friend_ids", new BasicDBList().addAll(friendIds));
			applicationUsersCollection.update(finder, new BasicDBObject("$set",
					updater));
		} else {
			// get new ID
			DBObject IDObj = applicationUsersCollection.findAndModify(
					new BasicDBObject("id", 0), new BasicDBObject("next_id",
							true), new BasicDBObject(), false,
					new BasicDBObject("$inc", new BasicDBObject("next_id", 1)),
					true, true);

			Long nextUserId = Long.parseLong(IDObj.get("next_id").toString());

			UUID accessToken = UUID.nameUUIDFromBytes(fbId.getBytes());
			BasicDBObject inserter = new BasicDBObject();
			inserter.append("id", nextUserId);
			inserter.append("access_token", accessToken);
			inserter.append("name", name);
			inserter.append("surname", surname);
			inserter.append("fb_access_token", fbAccessToken);
			inserter.append("fb_id", fbId);
			inserter.append("friend_ids", new BasicDBList().addAll(friendIds));
			applicationUsersCollection.insert(inserter);
		}

	}

	public static void main(String[] args) {
	}
}
