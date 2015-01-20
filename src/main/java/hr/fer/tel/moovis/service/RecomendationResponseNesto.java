package hr.fer.tel.moovis.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.recommendation.RecommendationRecord;
import hr.fer.tel.moovis.recommendation.RecommendationRecordWithFriendLikes;

@Service
public class RecomendationResponseNesto {

	public Set<RecommendationRecord> removeWatchedAndLikedMovies(
			Set<RecommendationRecord> recommendations, ApplicationUser user) {
		Set<RecommendationRecord> retSet = new HashSet<>();

		Set<String> likedMovies = user.getLikedMovieNames();
		Set<String> watchedMovies = user.getWatchedMovieNames();
		for (RecommendationRecord recomendation : recommendations) {
			if (!(likedMovies.contains(recomendation.getMovie().getTitle()) || watchedMovies
					.contains(recomendation.getMovie().getTitle()))) {
				retSet.add(recomendation);
			}
		}

		return retSet;
	}

	public Set<RecommendationRecord> getRecordsWtihFriendLikes(
			Set<RecommendationRecord> recommendations, ApplicationUser user) {

		Set<ApplicationUser> friends = user.getFriends();
		// System.out.println(user);
		// System.out.println(friends);

		Set<RecommendationRecord> setWithFriends = new HashSet<>();

		// System.out.println(recommendations);

		for (RecommendationRecord recommendation : recommendations) {
			// System.out.println(recommendation);
			boolean createdNewRecord = false;
			if (recommendation != null && recommendation.getMovie() != null
					&& recommendation.getMovie().getTitle() != null) {
				String movieName = recommendation.getMovie().getTitle();
				RecommendationRecordWithFriendLikes newRecord = null;

				for (ApplicationUser friend : friends) {
					if (friend.getLikedMovieNames().contains(movieName)) {
						// casted.addFriendName(builder.toString());
						if (newRecord == null) {
							newRecord = new RecommendationRecordWithFriendLikes(
									recommendation.getMovie(),
									recommendation.getRecScore());
							setWithFriends.add(newRecord);
							createdNewRecord = true;

						}
						StringBuilder builder = new StringBuilder();
						builder.append(friend.getName()).append(" ")
								.append(friend.getSurname());
						newRecord.addFriendName(builder.toString());
					}
				}
			}

			if (!createdNewRecord) {
				setWithFriends.add(recommendation);
			}
		}
		return setWithFriends;
	}
}