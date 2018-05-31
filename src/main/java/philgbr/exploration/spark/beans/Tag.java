package philgbr.exploration.spark.beans;


import java.io.Serializable;

@SuppressWarnings("serial")
public class Tag implements Serializable {

    private int userId;
    private int movieId;
    private String tag;
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
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
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
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) obj;
        if (movieId != other.movieId) {
            return false;
        }
        if (tag == null) {
            if (other.tag != null) {
                return false;
            }
        } else if (!tag.equals(other.tag)) {
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
        return "Tag [userId=" + userId + ", movieId=" + movieId + ", tag=" + tag + ", timestamp=" + timestamp + "]";
    }
}
