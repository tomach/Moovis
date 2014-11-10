package hr.fer.tel.moovis.model;

/**
 * Created by tomislaf on 9.11.2014..
 */
public class Movie {

    private String title;
    private String description;
    private String imdbId;
    private String tmdbId;
    private String rottenId;

    public Movie(String title, String description) {
        this.title = title;
        this.description = description;
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

    public String getRottenId() {
        return rottenId;
    }

    public void setRottenId(String rottenId) {
        this.rottenId = rottenId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "title = " + title + ", description = " + description;
    }
}
