package hr.fer.tel.moovis;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class MongoConnections {
	private static MongoConnections instance;
	static {
		try {
			instance = new MongoConnections();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	private final Mongo mongo;
	private final DB db;

	private MongoConnections() throws UnknownHostException {
		mongo = new MongoClient("localhost", 27017);
		db = mongo.getDB("moovis");
	}

	public synchronized static MongoConnections getInstance() {
		return instance;
	}

	public DB getDb() {
		return db;
	}

}
