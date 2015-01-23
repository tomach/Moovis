package hr.fer.tel.moovis.service;

import hr.fer.tel.moovis.MongoConnections;
import hr.fer.tel.moovis.apis.FacebookAPI;
import hr.fer.tel.moovis.dao.ApplicationUserDao;
import hr.fer.tel.moovis.exceptions.FacebookLoginException;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.names.MovieNamesContainer;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import facebook4j.Friend;
import facebook4j.Movie;
import facebook4j.User;

@Service
public class RegistrationService {
	@Autowired
	private ApplicationUserDao appUserRepo;
	@Autowired
	private FacebookProfileUpdater fbUpdater;

	private DBCollection rottenQueue;

	public RegistrationService() throws UnknownHostException {
		DB db = MongoConnections.getInstance().getDb();
		rottenQueue = db.getCollection("RottenSearchQueue");
	}

	public ApplicationUser registerApplicationUser(String facebookAccessToken)
			throws FacebookLoginException {

		ApplicationUser savedUser = null;

		try {

			FacebookAPI faceApi = new FacebookAPI(facebookAccessToken);
			System.out.println(faceApi);
			System.out.println(facebookAccessToken);
			User user;
			try {
				user = faceApi.getUser();
			} catch (Exception e) {
				throw new FacebookLoginException(
						"Error while getting facebook user."
								+ e.getLocalizedMessage());
			}
			String facebookId = user.getId();
			System.out.println(facebookId);
			System.out.println(appUserRepo);
			System.out.println(user.getName());
			System.out.println(user.getLastName());
			Set<ApplicationUser> friends;
			try {
				friends = getAllFacebookIds(faceApi.getFriends());
			} catch (Exception e1) {
				friends = new HashSet<>();
			}

			Set<String> likedMovieNames;

			ApplicationUser appUser = appUserRepo.findByFacebookId(facebookId);
			if (appUser != null) {
				appUser.setFacebookAccessToken(facebookAccessToken);
				appUser=fbUpdater.updateUserLikes(appUser);
				

				// OVO JE SAMO PRIVREMENO
				appUser.setName(user.getFirstName());
				appUser.setSurname(user.getLastName());
				//
				for (ApplicationUser friend : friends) {
					appUser.addFriend(friend);
				}
				appUser.setFacebookAccessToken(facebookAccessToken);
				appUser.setLastMovieLikesUpdateTS(System.currentTimeMillis() - 1000 * 60);
				savedUser = appUserRepo.save(appUser);
				addFriendToAppUser(friends, savedUser);
				return appUser;
			}

			String name = user.getFirstName();
			String surname = user.getLastName();
			String accessToken = UUID.nameUUIDFromBytes(facebookId.getBytes())
					.toString();

			MovieNamesContainer movieNamesChecker = MovieNamesContainer
					.getInstance();
			Set<String> checkedMovieNames = new HashSet<String>();
			try {
				likedMovieNames = getAllMovieNames(faceApi.getMovies(0));
			} catch (Exception e) {
				System.err.println("Error while fetching liked movies!");
				likedMovieNames = new HashSet<>();
			}

			System.out.println("Usporedba slicnosti!");

			for (String movie : likedMovieNames) {
				String checkedName = movieNamesChecker.getMovieName(movie);
				System.out.println(movie);
				System.out.println(checkedName);
				rottenQueue.insert(new BasicDBObject("movieKey", checkedName));
				checkedMovieNames.add(checkedName);
			}

			// System.out.println(faceApi.getFriends());
			// System.out.println(faceApi.getMovies(0));
			long lastFaceUpdate = System.currentTimeMillis() - 1000 * 60;
			ApplicationUser newUser = new ApplicationUser(accessToken,
					facebookId, facebookAccessToken, name, surname,
					lastFaceUpdate, checkedMovieNames, new HashSet<String>(),
					new HashSet<String>(), friends);
			savedUser = appUserRepo.save(newUser);

			addFriendToAppUser(friends, savedUser);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return savedUser;
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

	private void addFriendToAppUser(Set<ApplicationUser> usersToAddTo,
			ApplicationUser userToBeAdded) {
		for (ApplicationUser userToAddTo : usersToAddTo) {
			userToAddTo.addFriend(userToBeAdded);
			appUserRepo.save(userToAddTo);
		}
	}

}
