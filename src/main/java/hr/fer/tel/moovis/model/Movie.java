package hr.fer.tel.moovis.model;

/**
 * Created by tomislaf on 9.11.2014..
 */
public class Movie {

	private String title;
	private String imdbId;
	private String tmdbId;
	private double imdbScore;
	private double tmdbScore;

	public Movie(String title, String imdbId, String tmdbId, double imdbScore,
			double tmdbScore) {
		super();
		this.title = title;
		this.imdbId = imdbId;
		this.tmdbId = tmdbId;
		this.imdbScore = imdbScore;
		this.tmdbScore = tmdbScore;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public String getTmdbId() {
		return tmdbId;
	}

	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}

	public double getImdbScore() {
		return imdbScore;
	}

	public void setImdbScore(double imdbScore) {
		this.imdbScore = imdbScore;
	}

	public double getTmdbScore() {
		return tmdbScore;
	}

	public void setTmdbScore(double tmdbScore) {
		this.tmdbScore = tmdbScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tmdbId == null) ? 0 : tmdbId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (tmdbId == null) {
			if (other.tmdbId != null)
				return false;
		} else if (!tmdbId.equals(other.tmdbId))
			return false;
		return true;
	}

}
