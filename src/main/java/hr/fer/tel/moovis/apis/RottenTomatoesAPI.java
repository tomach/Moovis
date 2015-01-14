package hr.fer.tel.moovis.apis;

import java.util.ArrayList;
import java.util.List;

import com.omertron.rottentomatoesapi.RottenTomatoesApi;
import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.rottentomatoesapi.model.RTMovie;

/**
 * Created by Damjan on 9.11.2014..
 */
public class RottenTomatoesAPI {
	//LIMIT: 5 per sec, 10 000 per day
    private static final String API_KEY = "sdsque4c4t6gynrtv8znmrrz";
    
    private RottenTomatoesApi rotten;
    
    public RottenTomatoesAPI() throws RottenTomatoesException {
    	rotten = new RottenTomatoesApi(API_KEY); 
    }
    
    public RTMovie getRottenMovieByName(String movieName) {
    	List<RTMovie> rottenMovies = null;
    		
    	try {
			rottenMovies = rotten.getMoviesSearch(movieName);
		} catch (RottenTomatoesException e) {
			e.printStackTrace();
		}
    	
    	if (rottenMovies != null && rottenMovies.size() > 0) {
    		return rottenMovies.get(0);
    	} else {
    		return null;
    	}
    }
    
    private List<RTMovie> getSimilarMovies(String movieName) {
    	
    	List<RTMovie> similar = null;
    	RTMovie movie = getRottenMovieByName(movieName);
    	int id = getMovieID(movie);
    	
    	if (id != -1) {
    		try {
				similar = rotten.getMoviesSimilar(id);
			} catch (RottenTomatoesException e) {
				e.printStackTrace();
			}
    	}  	
    	return similar;
    }
    
    public List<String> getSimilarMovieNames(String movieName) {
    	List<String> movieNames = null;
    	List<RTMovie> similarMovies = getSimilarMovies(movieName);
    	
    	if (similarMovies != null && similarMovies.size() > 0) {
    		movieNames = new ArrayList<String>();
    		
    		for (RTMovie movie : similarMovies) {
        		movieNames.add(movie.getTitle());
        	}
    	}
    	
    	return movieNames;
    }
    
    private int getMovieID(RTMovie movie) {
    	if (movie != null) {
    		return movie.getId();
    	} else {
    		return -1;
    	}
    }
    
    
    public static void main(String[] args) throws RottenTomatoesException {
    	RottenTomatoesAPI rotten = new RottenTomatoesAPI();
    	System.out.println(rotten.getSimilarMovieNames("Shutter"));
    }
}
