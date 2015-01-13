package hr.fer.tel.moovis.searchers;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by filippm on 10.11.14..
 */
public class YouTubeSearch extends GenericSearch {
	private static final String MY_QUEUE = "YTSearchQueue";
	private static final Long NUMBER_OF_VIDEOS_RETURNED = Long.valueOf(1);
	private static final String API_KEY = "AIzaSyDZyb0QoNrpYYHNstDUyBehg5LrZOJZtZM";

	private YouTube youtube;

	public YouTubeSearch() throws UnknownHostException {
		super();
		// initialize YT api
		youtube = new YouTube.Builder(new NetHttpTransport(),
				new JacksonFactory(), new HttpRequestInitializer() {
					public void initialize(HttpRequest request)
							throws IOException {
					}
				}).setApplicationName("social-networks-project").build();
	}

	@Override
	protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {
		YouTube.Search.List search = null;
		try {
			search = youtube.search().list("id,snippet");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String queryTerm = obj.get("movieKey").toString() + " trailer";

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
			return;
		}

		System.out.println(searchResponse);

		List<SearchResult> searchResultList = searchResponse.getItems();
		if (searchResultList != null) {
			if (searchResultList.size() == 0)
				return;
			SearchResult result = searchResultList.get(0);
			if (result != null) {
				BasicDBObject movieDetails = new BasicDBObject();
				movieDetails
						.append("id", result.getId().getVideoId())
						.append("description",
								result.getSnippet().getDescription())
						.append("thumbnailURL",
								result.getSnippet().getThumbnails()
										.getDefault().getUrl())
						.append("title", result.getSnippet().getTitle());
				newMovieObject.append("youTube", movieDetails);
			}

		}
	}

	@Override
	protected DBCollection getQueue(DB db) {
		return db.getCollection(MY_QUEUE);
	}

	@Override
	protected long getSleepTime() {
		return 0;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(Thread.currentThread().getId());
		new Thread(new YouTubeSearch()).start();
	}
}
