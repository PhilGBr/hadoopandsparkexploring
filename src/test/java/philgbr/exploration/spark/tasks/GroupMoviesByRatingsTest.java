package philgbr.exploration.spark.tasks;

import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import philgbr.exploration.spark.utils.junit5.SparkTest;

import static philgbr.exploration.spark.tasks.MovieLensTables.RATINGS;


@SparkTest
public class GroupMoviesByRatingsTest {

    public SparkSession spark; // will be populated by @SparkTest

    @BeforeEach
    public void Setup()throws Exception {
        MovielensDbFixture.loadTable(spark, RATINGS, null);
    }
    @Test
    public  void hiveImpl1() {

        GroupMoviesByRatings find = new GroupMoviesByRatings();
        find.hiveImpl1(spark);

    }
    @Test
    public void dfopImpl1() {
        GroupMoviesByRatings find = new GroupMoviesByRatings();
        find.dfopImpl1(spark);
    }
    @Test
    public void dfopImpl2() {
        GroupMoviesByRatings find = new GroupMoviesByRatings();
        find.dfopImpl2(spark);
    }

    @Test
    public void dsImpl1() {
        GroupMoviesByRatings find = new GroupMoviesByRatings();
        find.dsImpl1(spark);
    }
    @Test
    public void dsImpl2() {
        GroupMoviesByRatings find = new GroupMoviesByRatings();
        find.dsImpl2(spark);
    }

    @Test
    public void rddImpl1() {
        GroupMoviesByRatings find = new GroupMoviesByRatings();
        find.rddImpl1(spark);
    }


    @Test
    public void rddImpl2() {
        GroupMoviesByRatings find = new GroupMoviesByRatings();
        find.rddImpl2(spark);
    }
}
