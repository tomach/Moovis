package hr.fer.tel.moovis.searchers.rotten;

import java.util.List;
import java.util.Map;

import com.omertron.rottentomatoesapi.RottenTomatoesApi;
import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.rottentomatoesapi.model.RTCast;
import com.omertron.rottentomatoesapi.model.RTClip;
import com.omertron.rottentomatoesapi.model.RTMovie;
import com.omertron.rottentomatoesapi.model.Review;

final public class RottenApiProxy extends RottenTomatoesApi {

	private static final Object SYNC_OBJ = new Object();

	private static String apiKey;

	private static RottenApiProxy instance;

	// variables for access control
	private long firstInvokeTime;
	private long lastInvokeTime;
	private int invokes;

	private RottenApiProxy(String apiKey) throws RottenTomatoesException {
		super(apiKey);
	}

	public static void setApiKey(String apiKey) {
		if (apiKey == null) {
			throw new IllegalStateException("Api key cannot be null!");
		}
		RottenApiProxy.apiKey = apiKey;
	}

	public synchronized static RottenApiProxy getInstance()
			throws RottenTomatoesException {
		if (apiKey == null) {
			throw new IllegalStateException(
					"Api key must be set before getting instance!");
		}
		if (instance == null) {
			instance = new RottenApiProxy(apiKey);
			instance.firstInvokeTime = System.nanoTime();
			instance.invokes = 0;
			instance.lastInvokeTime = System.nanoTime();
		}
		return instance;
	}

	private void countInvoke() {

		if (invokes == 0) {
			firstInvokeTime = System.nanoTime();
		}
		lastInvokeTime = System.nanoTime();
		invokes++;

	}

	private void checkTime() {
		lastInvokeTime = System.nanoTime();
		if (invokes == 5) {
			long duration = lastInvokeTime - firstInvokeTime;
			if (duration <= 5e6) {
				long timeToSleepInMs = (long) Math
						.floor(((double) duration) / 1_000_000);
				try {
					Thread.sleep(timeToSleepInMs);
				} catch (InterruptedException e) {
				}
			}
			invokes = 0;
		}

	}

	private void checkAccess() {
		synchronized (SYNC_OBJ) {
			checkTime();
			countInvoke();
		}
	}

	@Override
	public void setProxy(String host, int port, String username, String password) {
		checkAccess();
		super.setProxy(host, port, username, password);
	}

	@Override
	public void setTimeout(int webTimeoutConnect, int webTimeoutRead) {
		checkAccess();
		super.setTimeout(webTimeoutConnect, webTimeoutRead);
	}

	@Override
	public void setRetryDelay(long retryDelay) {
		checkAccess();
		super.setRetryDelay(retryDelay);
	}

	@Override
	public void setRetryLimit(int retryLimit) {
		checkAccess();
		super.setRetryLimit(retryLimit);
	}

	@Override
	public List<RTMovie> getBoxOffice(String country, int limit)
			throws RottenTomatoesException {
		checkAccess();
		return super.getBoxOffice(country, limit);
	}

	@Override
	public List<RTMovie> getBoxOffice() throws RottenTomatoesException {
		checkAccess();
		return super.getBoxOffice();
	}

	@Override
	public List<RTMovie> getBoxOffice(String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getBoxOffice(country);
	}

	@Override
	public List<RTMovie> getInTheaters(String country, int page, int pageLimit)
			throws RottenTomatoesException {
		checkAccess();
		return super.getInTheaters(country, page, pageLimit);
	}

	@Override
	public List<RTMovie> getInTheaters(String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getInTheaters(country);
	}

	@Override
	public List<RTMovie> getInTheaters() throws RottenTomatoesException {
		checkAccess();
		return super.getInTheaters();
	}

	@Override
	public List<RTMovie> getOpeningMovies(String country, int limit)
			throws RottenTomatoesException {
		checkAccess();
		return super.getOpeningMovies(country, limit);
	}

	@Override
	public List<RTMovie> getOpeningMovies(String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getOpeningMovies(country);
	}

	@Override
	public List<RTMovie> getOpeningMovies() throws RottenTomatoesException {
		checkAccess();
		return super.getOpeningMovies();
	}

	@Override
	public List<RTMovie> getUpcomingMovies(String country, int page,
			int pageLimit) throws RottenTomatoesException {
		checkAccess();
		return super.getUpcomingMovies(country, page, pageLimit);
	}

	@Override
	public List<RTMovie> getUpcomingMovies(String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getUpcomingMovies(country);
	}

	@Override
	public List<RTMovie> getUpcomingMovies() throws RottenTomatoesException {
		checkAccess();
		return super.getUpcomingMovies();
	}

	@Override
	public List<RTMovie> getTopRentals(String country, int limit)
			throws RottenTomatoesException {
		checkAccess();
		return super.getTopRentals(country, limit);
	}

	@Override
	public List<RTMovie> getTopRentals(String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getTopRentals(country);
	}

	@Override
	public List<RTMovie> getTopRentals() throws RottenTomatoesException {
		checkAccess();
		return super.getTopRentals();
	}

	@Override
	public List<RTMovie> getCurrentReleaseDvds(String country, int page,
			int pageLimit) throws RottenTomatoesException {
		checkAccess();
		return super.getCurrentReleaseDvds(country, page, pageLimit);
	}

	@Override
	public List<RTMovie> getCurrentReleaseDvds(String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getCurrentReleaseDvds(country);
	}

	@Override
	public List<RTMovie> getCurrentReleaseDvds() throws RottenTomatoesException {
		checkAccess();
		return super.getCurrentReleaseDvds();
	}

	@Override
	public List<RTMovie> getNewReleaseDvds(String country, int page,
			int pageLimit) throws RottenTomatoesException {
		checkAccess();
		return super.getNewReleaseDvds(country, page, pageLimit);
	}

	@Override
	public List<RTMovie> getNewReleaseDvds(String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getNewReleaseDvds(country);
	}

	@Override
	public List<RTMovie> getNewReleaseDvds() throws RottenTomatoesException {
		checkAccess();
		return super.getNewReleaseDvds();
	}

	@Override
	public List<RTMovie> getUpcomingDvds(String country, int page, int pageLimit)
			throws RottenTomatoesException {
		checkAccess();
		return super.getUpcomingDvds(country, page, pageLimit);
	}

	@Override
	public List<RTMovie> getUpcomingDvds(String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getUpcomingDvds(country);
	}

	@Override
	public List<RTMovie> getUpcomingDvds() throws RottenTomatoesException {
		checkAccess();
		return super.getUpcomingDvds();
	}

	@Override
	public RTMovie getDetailedInfo(int movieId) throws RottenTomatoesException {
		checkAccess();
		return super.getDetailedInfo(movieId);
	}

	@Override
	public List<RTCast> getCastInfo(int movieId) throws RottenTomatoesException {
		checkAccess();
		return super.getCastInfo(movieId);
	}

	@Override
	public List<RTClip> getMovieClips(int movieId)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMovieClips(movieId);
	}

	@Override
	public List<Review> getMoviesReviews(int movieId, String reviewType,
			int pageLimit, int page, String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesReviews(movieId, reviewType, pageLimit, page,
				country);
	}

	@Override
	public List<Review> getMoviesReviews(int movieId, String reviewType,
			String country) throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesReviews(movieId, reviewType, country);
	}

	@Override
	public List<Review> getMoviesReviews(int movieId, String country)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesReviews(movieId, country);
	}

	@Override
	public List<Review> getMoviesReviews(int movieId)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesReviews(movieId);
	}

	@Override
	public List<RTMovie> getMoviesSimilar(int movieId, int limit)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesSimilar(movieId, limit);
	}

	@Override
	public List<RTMovie> getMoviesSimilar(int movieId)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesSimilar(movieId);
	}

	@Override
	public RTMovie getMoviesAlias(String altMovieId, String type)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesAlias(altMovieId, type);
	}

	@Override
	public List<RTMovie> getMoviesSearch(String query, int pageLimit, int page)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesSearch(query, pageLimit, page);
	}

	@Override
	public List<RTMovie> getMoviesSearch(String query)
			throws RottenTomatoesException {
		checkAccess();
		return super.getMoviesSearch(query);
	}

	@Override
	public Map<String, String> getListsDirectory()
			throws RottenTomatoesException {
		checkAccess();
		return super.getListsDirectory();
	}

	@Override
	public Map<String, String> getMovieListsDirectory()
			throws RottenTomatoesException {
		checkAccess();
		return super.getMovieListsDirectory();
	}

	@Override
	public Map<String, String> getDvdListsDirectory()
			throws RottenTomatoesException {
		checkAccess();
		return super.getDvdListsDirectory();
	}

}
