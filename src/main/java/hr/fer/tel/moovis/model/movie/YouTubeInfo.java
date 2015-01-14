package hr.fer.tel.moovis.model.movie;

public class YouTubeInfo {

	private String thumbnailURL;
	private String title;
	private String description;
	private String id;

	public YouTubeInfo() {
	}

	public YouTubeInfo(String thumbnailURL, String title, String description,
			String id) {
		super();
		this.thumbnailURL = thumbnailURL;
		this.title = title;
		this.description = description;
		this.id = id;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		YouTubeInfo other = (YouTubeInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "YouTubeInfo [thumbnailURL=" + thumbnailURL + ", title=" + title
				+ ", description=" + description + ", id=" + id + "]";
	}

}
