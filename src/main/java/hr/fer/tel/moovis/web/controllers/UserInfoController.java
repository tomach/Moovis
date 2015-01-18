package hr.fer.tel.moovis.web.controllers;

import java.util.LinkedList;
import java.util.List;

import hr.fer.tel.moovis.dao.ApplicationUserRepository;
import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;
import hr.fer.tel.moovis.names.MovieNamesContainer;

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
public class UserInfoController {

	@Autowired
	private ApplicationUserRepository appUserRepo;

	@Autowired
	private MovieDao movieDao;

	@RequestMapping(value = "/user_info", method = RequestMethod.GET)
	public ResponseEntity<ApplicationUser> getUser(
			@RequestParam(value = "access_token") String accessToken) {
		System.out.println("Get user request!");
		System.out.println("acess_token:" + accessToken);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		System.out.println(user.getName());
		System.out.println(user.getSurname());
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
		// makni ga i iz watchliste
		user.removeMovieToWatchList(normalizedName);

		appUserRepo.save(user);
		JSONObject response = new JSONObject();
		response.put("sucsess", "true");
		response.put("status", "Movie added!");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

	}

	@RequestMapping(value = "/watched_movies/{name}", method = RequestMethod.DELETE)
	public ResponseEntity<String> removeWatchedMovie(
			@RequestParam(value = "access_token") String accessToken,
			@PathVariable(value = "name") String movieName) {
		System.out.println("Remove wathc movie request!");
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
		user.removeWatchedMovie(normalizedName);
		appUserRepo.save(user);
		JSONObject response = new JSONObject();
		response.put("sucsess", "true");
		response.put("status", "Movie removed!");
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

	@RequestMapping(value = "/watchlist/{name}", method = RequestMethod.DELETE)
	public ResponseEntity<String> removeMovieFromWatchList(
			@RequestParam(value = "access_token") String accessToken,
			@PathVariable(value = "name") String movieName) {
		System.out.println("Remove movie from watchlst request!");
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
		user.removeMovieToWatchList(normalizedName);
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