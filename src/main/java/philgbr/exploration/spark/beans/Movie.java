package philgbr.exploration.spark.beans;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class Movie implements Serializable {

    private int movieId;
    private String title;
    private String genres;
}
