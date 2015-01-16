package hr.fer.tel.moovis.model.movie;

public class TMDBMovieInfo {
	private String tmdbId;

	private long budget;
	private long revenue;

	private String releaseDate;
	private double voteAverage;
	private long voteCount;

	private String overview;

	private String photo;

	public TMDBMovieInfo() {
	}

	public String getTmdbId() {
		return tmdbId;
	}

	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}

	public long getBudget() {
		return budget;
	}

	public void setBudget(long budget) {
		this.budget = budget;
	}

	public long getRevenue() {
		return revenue;
	}

	public void setRevenue(long revenue) {
		this.revenue = revenue;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public double getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage(double voteAverage) {
		this.voteAverage = voteAverage;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(long voteCount) {
		this.voteCount = voteCount;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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
		TMDBMovieInfo other = (TMDBMovieInfo) obj;
		if (tmdbId == null) {
			if (other.tmdbId != null)
				return false;
		} else if (!tmdbId.equals(other.tmdbId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TMDBMovieInfo [tmdbId=" + tmdbId + ", budget=" + budget
				+ ", revenue=" + revenue + ", releaseDate=" + releaseDate
				+ ", voteAverage=" + voteAverage + ", voteCount=" + voteCount
				+ ", overview=" + overview + "]";
	}

}
