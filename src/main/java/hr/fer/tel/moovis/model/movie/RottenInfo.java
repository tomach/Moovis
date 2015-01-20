package hr.fer.tel.moovis.model.movie;

public class RottenInfo {
	private int rottenId;
	private String rottenTitle;
	private Long criticsScore;
	private Long audienceScore;

	public RottenInfo() {
		// TODO Auto-generated constructor stub
	}

	public RottenInfo(int rottenId, String rottenTitle, Long criticsScore,
			Long audienceScore) {
		super();
		this.rottenId = rottenId;
		this.rottenTitle = rottenTitle;
		this.criticsScore = criticsScore;
		this.audienceScore = audienceScore;
	}

	public long getRottenId() {
		return rottenId;
	}

	public void setRottenId(int rottenId) {
		this.rottenId = rottenId;
	}

	public String getRottenTitle() {
		return rottenTitle;
	}

	public void setRottenTitle(String rottenTitle) {
		this.rottenTitle = rottenTitle;
	}

	public Long getCriticsScore() {
		return criticsScore;
	}

	public void setCriticsScore(Long criticsScore) {
		this.criticsScore = criticsScore;
	}

	public Long getAudienceScore() {
		return audienceScore;
	}

	public void setAudienceScore(Long audienceScore) {
		this.audienceScore = audienceScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (rottenId ^ (rottenId >>> 32));
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
		RottenInfo other = (RottenInfo) obj;
		if (rottenId != other.rottenId)
			return false;
		return true;
	}

}
