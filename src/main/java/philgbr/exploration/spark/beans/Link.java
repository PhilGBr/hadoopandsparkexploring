package philgbr.exploration.spark.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Link implements Serializable {

    private int movieId;
    private int imdbId;
    private int tmbdId;
    
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getImdbId() {
        return imdbId;
    }

    public void setImdbId(int imdbId) {
        this.imdbId = imdbId;
    }

    public int getTmbdId() {
        return tmbdId;
    }

    public void setTmbdId(int tmbdId) {
        this.tmbdId = tmbdId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + imdbId;
        result = prime * result + movieId;
        result = prime * result + tmbdId;
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
        if (!(obj instanceof Link)) {
            return false;
        }
        Link other = (Link) obj;
        if (imdbId != other.imdbId) {
            return false;
        }
        if (movieId != other.movieId) {
            return false;
        }
        if (tmbdId != other.tmbdId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Link [movieId=" + movieId + ", imdbId=" + imdbId + ", tmbdId=" + tmbdId + "]";
    }

}
