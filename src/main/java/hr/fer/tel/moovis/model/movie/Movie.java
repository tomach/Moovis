package hr.fer.tel.moovis.model.movie;

import hr.fer.tel.moovis.model.Person;

import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

/**
 * Created by tomislaf on 9.11.2014..
 */
public class Movie {

	private String title;

	private YouTubeInfo ytInfo;

	private IMDBMovieInfo imdbInfo;

	private TMDBMovieInfo tmdbInfo;

	private List<String> genres;
	private List<Person> cast;
	private List<Person> directors;

	private List<Movie> similarMovies;

	public Movie() {
	}

	public Movie(String title, YouTubeInfo ytInfo, IMDBMovieInfo imdbInfo,
			TMDBMovieInfo tmdbInfo, List<String> genres, List<Person> cast,
			List<Person> directors, List<Movie> similarMovies) {
		super();
		this.title = title;
		this.ytInfo = ytInfo;
		this.imdbInfo = imdbInfo;
		this.tmdbInfo = tmdbInfo;
		this.genres = genres;
		this.cast = cast;
		this.directors = directors;
		this.similarMovies = similarMovies;
	}

	public YouTubeInfo getYtInfo() {
		return ytInfo;
	}

	public void setYtInfo(YouTubeInfo ytInfo) {
		this.ytInfo = ytInfo;
	}

	public IMDBMovieInfo getImdbInfo() {
		return imdbInfo;
	}

	public void setImdbInfo(IMDBMovieInfo imdbInfo) {
		this.imdbInfo = imdbInfo;
	}

	public TMDBMovieInfo getTmdbInfo() {
		return tmdbInfo;
	}

	public void setTmdbInfo(TMDBMovieInfo tmdbInfo) {
		this.tmdbInfo = tmdbInfo;
	}

	public List<Movie> getSimilarMovies() {
		return similarMovies;
	}

	public void setSimilarMovies(List<Movie> similarMovies) {
		this.similarMovies = similarMovies;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public List<Person> getCast() {
		return cast;
	}

	public void setCast(List<Person> cast) {
		this.cast = cast;
	}

	public List<Person> getDirectors() {
		return directors;
	}

	public void setDirectors(List<Person> directors) {
		this.directors = directors;
	}

	@Override
	public String toString() {
		return "Movie [title=" + title + ", ytInfo=" + ytInfo + ", imdbInfo="
				+ imdbInfo + ", tmdbInfo=" + tmdbInfo + ", genres=" + genres
				+ ", cast=" + cast + ", directors=" + directors
				+ ", similarMovies=" + similarMovies + "]";
	}

}