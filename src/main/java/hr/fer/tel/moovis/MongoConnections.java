package hr.fer.tel.moovis;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoConnections {

	public static DB getMongoDatabase() throws UnknownHostException {
		if (System.getenv("OPENSHIFT_MONGODB_DB_HOST") != null) {

			String openshiftMongoDbHost = System
					.getenv("OPENSHIFT_MONGODB_DB_HOST");
			int openshiftMongoDbPort = Integer.parseInt(System
					.getenv("OPENSHIFT_MONGODB_DB_PORT"));
			String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
			String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
			String databaseName = System.getenv("OPENSHIFT_APP_NAME");

			MongoCredential cred = MongoCredential.createMongoCRCredential(
					username, databaseName, password.toCharArray());

			MongoClient mongo = new MongoClient(new ServerAddress(
					openshiftMongoDbHost, openshiftMongoDbPort),
					Arrays.asList(cred));
			System.out.println(openshiftMongoDbHost);
			System.out.println(openshiftMongoDbPort);
			System.out.println(username);
			System.out.println(password);
			System.out.println(databaseName);
			System.out.println(mongo);
			System.out.println(mongo.getDB(databaseName));
			return mongo.getDB(databaseName);
		} else {

			return new MongoClient("localhost", 27017).getDB("moovis");
		}
	}
}
