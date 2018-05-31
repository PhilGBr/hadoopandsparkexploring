package philgbr.exploration.spark.beans;


import java.io.Serializable;

@SuppressWarnings("serial")
public class Rating implements Serializable {

    private int userId;
    private int movieId;
    private float rating;
    private int timestamp;
    
    
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getMovieId() {
        return movieId;
    }
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public int getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + movieId;
        result = prime * result + Float.floatToIntBits(rating);
        result = prime * result + timestamp;
        result = prime * result + userId;
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
        if (!(obj instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) obj;
        if (movieId != other.movieId) {
            return false;
        }
        if (Float.floatToIntBits(rating) != Float.floatToIntBits(other.rating)) {
            return false;
        }
        if (timestamp != other.timestamp) {
            return false;
        }
        if (userId != other.userId) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "Rating [userId=" + userId + ", movieId=" + movieId + ", rating=" + rating + ", timestamp=" + timestamp
                + "]";
    }
}
