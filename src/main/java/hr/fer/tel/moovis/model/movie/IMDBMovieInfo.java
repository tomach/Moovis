package hr.fer.tel.moovis.model.movie;

public class IMDBMovieInfo {

	private String imdbId;
	private double rating;

	public IMDBMovieInfo() {
	}

	public IMDBMovieInfo(String imdbId, double rating) {
		super();
		this.imdbId = imdbId;
		this.rating = rating;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imdbId == null) ? 0 : imdbId.hashCode());
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
		IMDBMovieInfo other = (IMDBMovieInfo) obj;
		if (imdbId == null) {
			if (other.imdbId != null)
				return false;
		} else if (!imdbId.equals(other.imdbId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IMDBMovieInfo [imdbId=" + imdbId + ", rating=" + rating + "]";
	}

}
