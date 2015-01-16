package hr.fer.tel.moovis.web.controllers;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import hr.fer.tel.moovis.dao.ApplicationUserRepository;
import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.exceptions.FacebookLoginException;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;
import hr.fer.tel.moovis.names.MovieNamesContainer;
import hr.fer.tel.moovis.recommendation.MovieRecommendation;
import hr.fer.tel.moovis.recommendation.RecommendationRecord;
import hr.fer.tel.moovis.service.RegistrationService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
	private MovieDao movieDao;
	@Autowired
	private MovieRecommendation movieRec;
	@Autowired
	private ApplicationUserRepository appUserRepo;

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

	@RequestMapping(value = "/movie/{name}", method = RequestMethod.GET)
	public ResponseEntity<Movie> getMovie(
			@PathVariable(value = "name") String movieName) {

		Movie mov = movieDao.findMovieByName(MovieNamesContainer.getInstance()
				.getMovieName(movieName));
		System.out.println(mov);
		return new ResponseEntity<Movie>(mov, HttpStatus.OK);
	}

	@RequestMapping(value = "/rec", method = RequestMethod.GET)
	public ResponseEntity<List<Movie>> env1(
			@RequestParam(value = "access_token") String accessToken) {

		System.out.println(accessToken);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		System.out.println(user);
		List<Movie> rec = movieRec.calculateRecommendation(user);
		if (rec.size() > 50) {
			rec = rec.subList(0, 50);
		}

		return new ResponseEntity<List<Movie>>(rec, HttpStatus.OK);

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

}
