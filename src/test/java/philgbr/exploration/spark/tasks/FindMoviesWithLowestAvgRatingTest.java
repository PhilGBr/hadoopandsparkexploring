package philgbr.exploration.spark.tasks;

import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import philgbr.exploration.spark.utils.junit5.SparkTest;

import static philgbr.exploration.spark.tasks.MovieLensTables.RATINGS;


@SparkTest
public class FindMoviesWithLowestAvgRatingTest {

    public SparkSession spark; // will be populated by @SparkTest

    @BeforeEach
    public void Setup()throws Exception {
        MovielensDbFixture.loadTable(spark, RATINGS, null);
    }
    @Test
    public  void hiveImpl1() {

        FindMoviesWithLowestAvgRating find = new FindMoviesWithLowestAvgRating();
        find.hiveImpl1(spark, 1, 1);

    }
    @Test
    public void dfopImpl1() {
        FindMoviesWithLowestAvgRating find = new FindMoviesWithLowestAvgRating();
        find.dfopImpl1(spark, 1, 1);
    }

    @Test
    public void rddImpl1() {
        FindMoviesWithLowestAvgRating find = new FindMoviesWithLowestAvgRating();
        find.rddImpl1(spark, 1, 1);
    }

}
