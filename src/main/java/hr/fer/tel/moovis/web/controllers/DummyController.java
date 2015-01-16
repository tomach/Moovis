package hr.fer.tel.moovis.web.controllers;

import java.util.LinkedList;
import java.util.List;

import hr.fer.tel.moovis.dao.ApplicationUserRepository;
import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.exceptions.FacebookLoginException;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;
import hr.fer.tel.moovis.names.MovieNamesContainer;
import hr.fer.tel.moovis.recommendation.MovieRecommendationImpl;
import hr.fer.tel.moovis.recommendation.MovieRecommendationOnlyFriendScoreImpl;
import hr.fer.tel.moovis.recommendation.MovieRecommendationWithFriendScoreImpl;
import hr.fer.tel.moovis.recommendation.RecommendationRecord;
import hr.fer.tel.moovis.service.RegistrationService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DummyController {

	@Autowired
	private MovieRecommendationOnlyFriendScoreImpl movieRecOnlyFriend;
	@Autowired
	private MovieDao movieDao;
	@Autowired
	private MovieRecommendationImpl movieRecSimilar;
	@Autowired
	private ApplicationUserRepository appUserRepo;
	@Autowired
	private MovieRecommendationWithFriendScoreImpl movieRecWithFriends;

	@Autowired
	private RegistrationService regService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public void facebookLogin() {

		try {
			regService
					.registerApplicationUser("CAAHplTHhoNABAFzZBQ8BXJMZCEJCtvPdpf6KY0SeiSlVoN8GITAtLwPRy1OUpOIVGcSZBINZBCpyfM2VurfZBw4SgjeG0NIEJ422wxvYfG14syYrjER6o2RVo4D4vGJqgigbEhSfAhbnstXOyV771jgpdyHGzfcqG9ebXmaUP4EnpDx35SaT32GpmBkiFu7Xte25FtAifeaJxAQg1y2WA");
		} catch (FacebookLoginException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/test_rec", method = RequestMethod.GET)
	public void testRec() {

		ApplicationUser user = appUserRepo
				.findByAccessToken("02eb2504-17f8-33c3-8d25-9325b0235201");
		movieRecOnlyFriend.calculateRecommendation(user);

	}

	@RequestMapping(value = "/test2_rec_all", method = RequestMethod.GET)
	public void testRecAll() {

		ApplicationUser user = appUserRepo
				.findByAccessToken("02eb2504-17f8-33c3-8d25-9325b0235201");
		movieRecWithFriends.calculateRecommendation(user);

	}

	@RequestMapping(value = "/movie/{name}", method = RequestMethod.GET)
	public ResponseEntity<Movie> getMovie(
			@PathVariable(value = "name") String movieName) {

		Movie mov = movieDao.findMovieByName(MovieNamesContainer.getInstance()
				.getMovieName(movieName));
		System.out.println(mov);
		return new ResponseEntity<Movie>(mov, HttpStatus.OK);
	}

	@RequestMapping(value = "/rec", method = RequestMethod.GET)
	public ResponseEntity<List<RecommendationRecord>> env1(
			@RequestParam(value = "access_token") String accessToken,
			@RequestParam(value = "type") String type) {

		//all, friends, similar
		System.out.println(accessToken);
		System.out.println(type);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		System.out.println(user);
		
		List<RecommendationRecord> rec;
		if (type.equals("all")) {
			rec = movieRecWithFriends.calculateRecommendation(user);
		} else if (type.equals("similar")) {
			rec = movieRecSimilar.calculateRecommendation(user);
		} else {
			rec = movieRecOnlyFriend.calculateRecommendation(user);
		}
		
		if (rec.size() > 50) {
			rec = rec.subList(0, 50);
		}

		return new ResponseEntity<List<RecommendationRecord>>(rec, HttpStatus.OK);
	}

	@RequestMapping(value = "/user_info", method = RequestMethod.GET)
	public ResponseEntity<ApplicationUser> getUser(
			@RequestParam(value = "access_token") String accessToken) {
		System.out.println("Get user request!");
		System.out.println("acess_token:" + accessToken);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);

		return new ResponseEntity<ApplicationUser>(user, HttpStatus.OK);

	}

	@RequestMapping(value = "/watched_movies/{name}", method = RequestMethod.POST)
	public ResponseEntity<String> addWatchedMovie(
			@RequestParam(value = "access_token") String accessToken,
			@PathVariable(value = "name") String movieName) {
		System.out.println("Add wathc movie request!");
		System.out.println("access token:" + accessToken);
		System.out.println("Movie name:" + movieName);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		System.out.println(user);
		if (user == null) {
			JSONObject response = new JSONObject();
			response.put("sucsess", "false");
			response.put("error", "User not found");

			return new ResponseEntity<String>(response.toString(),
					HttpStatus.BAD_REQUEST);
		}

		String normalizedName = MovieNamesContainer.getInstance().getMovieName(
				movieName);
		user.addWatchedMovie(normalizedName);
		appUserRepo.save(user);
		JSONObject response = new JSONObject();
		response.put("sucsess", "true");
		response.put("status", "Movie added!");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

	}

	@RequestMapping(value = "/watchlist/{name}", method = RequestMethod.POST)
	public ResponseEntity<String> addMovieToWatchList(
			@RequestParam(value = "access_token") String accessToken,
			@PathVariable(value = "name") String movieName) {
		System.out.println("Add movie to watchlst request!");
		System.out.println("access token:" + accessToken);
		System.out.println("Movie name:" + movieName);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		System.out.println(user);
		if (user == null) {
			JSONObject response = new JSONObject();
			response.put("sucsess", "false");
			response.put("error", "User not found");

			return new ResponseEntity<String>(response.toString(),
					HttpStatus.BAD_REQUEST);
		}

		String normalizedName = MovieNamesContainer.getInstance().getMovieName(
				movieName);
		user.addMovieToWatchList(normalizedName);
		appUserRepo.save(user);
		JSONObject response = new JSONObject();
		response.put("sucsess", "true");
		response.put("status", "Movie added!");

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/watchlist", method = RequestMethod.GET)
	public ResponseEntity<List<Movie>> getWatchList(
			@RequestParam(value = "access_token") String accessToken) {
		System.out.println("Get wathclist request!");
		System.out.println("access token:" + accessToken);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		System.out.println(user);
		if (user == null) {
			return new ResponseEntity<List<Movie>>(HttpStatus.BAD_REQUEST);
		}

		List<Movie> retList = new LinkedList<Movie>();
		for (String watchlistMovieName : user.getWatchList()) {
			Movie mov = movieDao.findMovieByName(watchlistMovieName);
			if (mov != null) {
				retList.add(mov);
			}

		}
		return new ResponseEntity<List<Movie>>(retList, HttpStatus.OK);
	}

	@RequestMapping(value = "/watchedlist", method = RequestMethod.GET)
	public ResponseEntity<List<Movie>> getWatchedList(
			@RequestParam(value = "access_token") String accessToken) {
		System.out.println("Get watchedlist request!");
		System.out.println("access token:" + accessToken);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		System.out.println(user);
		if (user == null) {
			return new ResponseEntity<List<Movie>>(HttpStatus.BAD_REQUEST);
		}

		List<Movie> retList = new LinkedList<Movie>();
		for (String watchlistMovieName : user.getWatchedMovieNames()) {
			Movie mov = movieDao.findMovieByName(watchlistMovieName);
			if (mov != null) {
				retList.add(mov);
			}

		}
		return new ResponseEntity<List<Movie>>(retList, HttpStatus.OK);
	}
}
