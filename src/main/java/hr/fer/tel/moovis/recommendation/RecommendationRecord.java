package hr.fer.tel.moovis.recommendation;

import hr.fer.tel.moovis.model.movie.Movie;

public class RecommendationRecord implements Comparable<RecommendationRecord> {
	private Movie movie;
	private double recScore;

	public RecommendationRecord(Movie movie, double recScore) {
		super();
		this.movie = movie;
		this.recScore = recScore;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public double getRecScore() {
		return recScore;
	}

	public void setRecScore(double recScore) {
		this.recScore = recScore;
	}

	@Override
	public String toString() {
		return "RecommendationRecord [movie=" + movie + ", recScore="
				+ recScore + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((movie == null) ? 0 : movie.hashCode());
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
		RecommendationRecord other = (RecommendationRecord) obj;
		if (movie == null) {
			if (other.movie != null)
				return false;
		} else if (!movie.equals(other.movie))
			return false;
		return true;
	}

	@Override
	public int compareTo(RecommendationRecord o) {
		if (this.recScore > o.recScore) {
			return -1;
		} else if (this.recScore < o.recScore) {
			return 1;
		} else {
			return 0;
		}
	}
}