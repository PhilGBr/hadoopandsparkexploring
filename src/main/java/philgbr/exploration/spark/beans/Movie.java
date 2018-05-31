package philgbr.exploration.spark.beans;


import java.io.Serializable;


@SuppressWarnings("serial")
public class Movie implements Serializable {

    private int movieId;
    private String title;
    private String genres;
    
    public int getMovieId() {
        return movieId;
    }
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getGenres() {
        return genres;
    }
    public void setGenres(String genres) {
        this.genres = genres;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genres == null) ? 0 : genres.hashCode());
        result = prime * result + movieId;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) obj;
        if (genres == null) {
            if (other.genres != null) {
                return false;
            }
        } else if (!genres.equals(other.genres)) {
            return false;
        }
        if (movieId != other.movieId) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "Movie [movieId=" + movieId + ", title=" + title + ", genres=" + genres + "]";
    }
}
