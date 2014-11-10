package hr.fer.tel.moovis.searchers;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.mongodb.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by filippm on 09.11.14..
 */
public class YouTubeSearch implements Runnable {
    private static final String DB_NAME = "moovis";
    private static final String MY_QUEUE = "YTSearchQueue";
    private static final int SLEEP_TIME = 100 * 60;
    private static final Long NUMBER_OF_VIDEOS_RETURNED = Long.valueOf(5);
    private static final String API_KEY = "AIzaSyDZyb0QoNrpYYHNstDUyBehg5LrZOJZtZM";


    private MongoClient mongo;
    private YouTube youtube;

    public YouTubeSearch() throws UnknownHostException {
        // Since 2.10.0, uses MongoClient
        mongo = new MongoClient("localhost", 27017);

        //initialize YT api
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("social-networks-project").build();
    }

    public void run() {
        System.out.println(Thread.currentThread().getId());

        while (true) {
            searchProcess();
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchProcess() {
        DB db = mongo.getDB(DB_NAME);
        DBCollection table = db.getCollection(MY_QUEUE);

        DBCursor cursor = table.find();
        DBObject obj;

        try {
            while (cursor.hasNext()) {
                //Dohvati iz reda
                obj = cursor.next();
                table.remove(obj);
                System.out.println(obj);
                String movieKey = obj.get("movieKey").toString();
                String movieName = obj.get("name").toString();
                String movieYear = obj.get("year").toString();
                System.out.println(movieKey);
                System.out.println(movieName);
                System.out.println(movieYear);

                //Obrada uz YT api
                YouTube.Search.List search = null;
                try {
                    search = youtube.search().list("id,snippet");
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                String queryTerm = movieName;

                search.setKey(API_KEY);
                search.setQ(queryTerm);
                search.setType("video");

                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

                SearchListResponse searchResponse = null;
                try {
                    searchResponse = search.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                List<SearchResult> searchResultList = searchResponse.getItems();
                if (searchResultList != null) {
                    for (SearchResult res : searchResultList) {
                        System.out.println(res);
                    }
                }
            }
        } finally {
            cursor.close();
        }


    }


    public static void main(String[] args) throws IOException {


        System.out.println(Thread.currentThread().getId());
        new Thread(new YouTubeSearch()).start();
    }


}
