package hr.fer.tel.moovis.web.controllers;

import hr.fer.tel.moovis.dao.ApplicationUserDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.recommendation.MovieRecommendationImpl;
import hr.fer.tel.moovis.recommendation.MovieRecommendationOnlyFriendScoreImpl;
import hr.fer.tel.moovis.recommendation.MovieRecommendationWithFriendScoreImpl;
import hr.fer.tel.moovis.recommendation.RecommendationRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieRecommendationController {
	
	private static final String REC_ALL = "all";
	private static final String REC_SIMILAR = "similar";
	private static final String REC_FRIENDS = "friends";
	
	@Autowired
	private MovieRecommendationOnlyFriendScoreImpl movieRecOnlyFriend;
	@Autowired
	private MovieRecommendationImpl movieRecSimilar;
	@Autowired
	private ApplicationUserDao appUserRepo;
	@Autowired
	private MovieRecommendationWithFriendScoreImpl movieRecAll;
	

	@RequestMapping(value = "/rec", method = RequestMethod.GET)
	public ResponseEntity<List<RecommendationRecord>> getRecommendation (
			@RequestParam(value = "access_token") String accessToken,
			@RequestParam(value = "type") String type) {

		//all, friends, similar
		//System.out.println(accessToken);
		//System.out.println(type);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		//System.out.println(user);
		
		List<RecommendationRecord> rec = null;
		
		if (type.equals(REC_ALL)) {
			rec = movieRecAll.calculateRecommendation(user);
		} else if (type.equals(REC_SIMILAR)) {
			rec = movieRecSimilar.calculateRecommendation(user);
		} else if (type.equals(REC_FRIENDS)) {
			rec = movieRecOnlyFriend.calculateRecommendation(user);
		}
		
		if (rec != null && rec.size() > 50) {
			rec = rec.subList(0, 50);
		}

		return new ResponseEntity<List<RecommendationRecord>>(rec, HttpStatus.OK);
	}
}