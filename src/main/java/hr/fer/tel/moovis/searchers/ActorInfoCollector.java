package hr.fer.tel.moovis.searchers;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Person;

public class ActorInfoCollector {
	private static final String DB_NAME = "moovis";
	private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";

	private DB db;
	private DBCollection actorInfoQueue;
	private DBCollection actorInfoCollection;
	private TheMovieDbApi theMovieDbApi;

	public ActorInfoCollector() throws UnknownHostException, MovieDbException {
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		db = mongo.getDB(DB_NAME);

		actorInfoQueue = db.getCollection("ActorInfoQueue");
		actorInfoCollection = db.getCollection("ActorInfo");
		theMovieDbApi = new TheMovieDbApi(API_KEY);

	}

	public void process() {
		DBCursor cur = actorInfoQueue.find();
		cur.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

		int i = 0;
		long startTime;
		long endTime;
		long duration;
		while (cur.hasNext()) {
			startTime = System.currentTimeMillis();
			if (i % 1000 == 0) {
				System.out.println(i+"/"+cur.count());
			}
			DBObject actorRequest = cur.next();
			actorInfoQueue.remove(actorRequest);

			Integer actorId = Integer.parseInt(actorRequest.get("tmdbId")
					.toString());
			Person actor = null;
			try {
				actor = theMovieDbApi.getPersonInfo(actorId);
				i++;
			} catch (MovieDbException e) {
				e.printStackTrace();
				continue;
			}
			BasicDBObject actorInfo = new BasicDBObject();
			actorInfo.append("tmdbId", actor.getId());
			actorInfo.append("name", actor.getName());
			actorInfo.append("biography", actor.getBiography());
			actorInfo.append("picture", actor.getProfilePath());
			actorInfo.append("birthday", actor.getBirthday());
			actorInfo.append("birthplace", actor.getBirthplace());
			actorInfo.append("deathday", actor.getDeathday());
			actorInfo.append("popularity", actor.getPopularity());

			this.actorInfoCollection.insert(actorInfo);
			endTime = System.currentTimeMillis();
			duration = endTime - startTime;
			if (duration < 330) {
				try {
					if ((330 - duration) > 0) {
						Thread.sleep(330 - duration);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException,
			MovieDbException {

		new ActorInfoCollector().process();
	}
}
