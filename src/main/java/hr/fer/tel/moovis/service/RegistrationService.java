package hr.fer.tel.moovis.service;

import hr.fer.tel.moovis.apis.FacebookAPI;
import hr.fer.tel.moovis.dao.ApplicationUserRepository;
import hr.fer.tel.moovis.model.ApplicationUser;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import facebook4j.FacebookException;
import facebook4j.Friend;
import facebook4j.Movie;
import facebook4j.User;
import facebook4j.internal.org.json.JSONException;

@Service
public class RegistrationService {
	@Autowired
	private ApplicationUserRepository appUserRepo;

	public void registerApplicationUser(String facebookAccessToken) {

		try {

			FacebookAPI faceApi = new FacebookAPI(facebookAccessToken);

			User user = faceApi.getUser();
			String facebookId = user.getId();

			if (appUserRepo.findByFacebookId(facebookId) != null) {
				throw new IllegalStateException();
			}

			String name = user.getFirstName();
			String surname = user.getLastName();
			String accessToken = UUID.nameUUIDFromBytes(facebookId.getBytes())
					.toString();

			Set<String> likedMovieNames = getAllMovieNames(faceApi.getMovies(0));
			Set<ApplicationUser> friends = getAllFacebookIds(faceApi
					.getFriends());
			System.out.println(faceApi.getFriends());
			System.out.println(faceApi.getMovies(0));

			// TODO add call to dao save
			ApplicationUser newUser = new ApplicationUser(accessToken,
					facebookId, facebookAccessToken, name, surname,
					likedMovieNames, friends);
			appUserRepo.save(newUser);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FacebookException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private Set<String> getAllMovieNames(List<Movie> movies) {
		Set<String> movieNames = new HashSet<>();
		for (Movie movie : movies) {
			movieNames.add(movie.getName());
		}
		return movieNames;
	}

	private Set<ApplicationUser> getAllFacebookIds(List<Friend> friends) {
		Set<ApplicationUser> facebookIds = new HashSet<>();
		for (Friend friend : friends) {
			ApplicationUser fr = appUserRepo.findByFacebookId(friend.getId());
			if (fr != null) {
				facebookIds.add(fr);
			}
		}
		return facebookIds;
	}
}
