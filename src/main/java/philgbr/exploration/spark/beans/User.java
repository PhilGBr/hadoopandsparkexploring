package philgbr.exploration.spark.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
    
    private String userId;
    private String segmentRater;
    private String segmentTagger;
    
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getSegmentRater() {
        return segmentRater;
    }
    public void setSegmentRater(String segmentRater) {
        this.segmentRater = segmentRater;
    }
    public String getSegmentTagger() {
        return segmentTagger;
    }
    public void setSegmentTagger(String segmentTagger) {
        this.segmentTagger = segmentTagger;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((segmentRater == null) ? 0 : segmentRater.hashCode());
        result = prime * result + ((segmentTagger == null) ? 0 : segmentTagger.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        if (segmentRater == null) {
            if (other.segmentRater != null) {
                return false;
            }
        } else if (!segmentRater.equals(other.segmentRater)) {
            return false;
        }
        if (segmentTagger == null) {
            if (other.segmentTagger != null) {
                return false;
            }
        } else if (!segmentTagger.equals(other.segmentTagger)) {
            return false;
        }
        if (userId == null) {
            if (other.userId != null) {
                return false;
            }
        } else if (!userId.equals(other.userId)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "User [userId=" + userId + ", segmentRater=" + segmentRater + ", segmentTagger=" + segmentTagger + "]";
    }

}
