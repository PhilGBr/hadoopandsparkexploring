package philgbr.exploration.spark.beans;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class Tag implements Serializable {

    private int userId;
    private int movieId;
    private String tag;
    private String timestamp;
}
