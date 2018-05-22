package philgbr.exploration.spark.tasks;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import philgbr.exploration.spark.beans.Rating;
import philgbr.exploration.spark.utils.LogMyTime;
import scala.Tuple2;

import static org.apache.spark.sql.functions.col;
import static philgbr.exploration.spark.tasks.MovieLensTables.RATINGS;

public class GroupMoviesByRatings {

    private static final Logger LOG = LoggerFactory.getLogger(GroupMoviesByRatings.class);

    private static final String QRY_MOVIE_DISTRIBUTION = "select rating, count(*) as nb_ratings " +
                                                            "from ratings "+
                                                            "group by rating ";

    /**
     *  Dataframe implementation 1.
     *
     */
    @LogMyTime
    void hiveImpl1(SparkSession spark) {

        spark.sql(QRY_MOVIE_DISTRIBUTION).show(true);
    }

    @LogMyTime
    void dfopImpl1(SparkSession spark) {

        spark.table(RATINGS.toString())
                .select(col("rating"))
                .groupBy(col("rating"))
                .count()
                .show(true);
    }

    /**
     * Same as {@link #dfopImpl1(SparkSession)} except we removed the projection so we can measure the impact of such omission
     */

    @LogMyTime
    void dfopImpl2(SparkSession spark) {

        spark.table(RATINGS.toString())
              //.select(col("rating")) // oups !
                .groupBy(col("rating"))
                .count()
                .show(true);
    }

    /**
     * Implementation based on the strongly typed Dataset API.
     */
     @LogMyTime
     void dsImpl1(SparkSession spark) {

         Dataset<Float> ds = spark.table(RATINGS.toString())
                                .select("rating").as(Encoders.FLOAT());

         RelationalGroupedDataset groupDs= ds.groupBy(col("rating"));
         Dataset<Row> countDf= groupDs.count();
         countDf.show(true);
     }
    /**
     * Implementation based on the strongly typed Dataset API, with user defined types encoding.<p>
     *
     */
    @LogMyTime
    void dsImpl2(SparkSession spark) {

        Encoder<Rating> ratingEncoder = Encoders.bean(Rating.class);

        //Dataset<Rating> ds = spark.table(RATINGS.toString()).as(ratingEncoder);
            // the line above does not work as column names (ex.: 'movie_id') and field names (ex.: 'movieId') doest not match

        Dataset<Rating> ds =
                spark.table(RATINGS.toString())
                .selectExpr("movie_id as movieId", "user_id as userId", "rating", "timestamp")
                .as(ratingEncoder);

        Dataset<Float> fds=ds.select(col("rating")).as(Encoders.FLOAT());

        RelationalGroupedDataset groupDs = fds.groupBy(col("rating"));
        Dataset<Row> countDf= groupDs.count();
        countDf.show(true);
    }

    /**
     * RDD implementation (
     *
     */
    @LogMyTime
    void rddImpl1(SparkSession spark) {
        Encoder<Rating> ratingEncoder = Encoders.bean(Rating.class);

        JavaRDD<Rating> typedRdd = spark.table(RATINGS.toString())
                                    .selectExpr("movie_id as movieId", "user_id as userId", "rating", "timestamp")
                                    .as(ratingEncoder).toJavaRDD();

        JavaPairRDD<Float, Iterable<Rating>> typedPairRdd = typedRdd.groupBy(Rating::getRating);

        typedPairRdd.foreach((Tuple2<Float, Iterable<Rating>> t) ->
                            {LOG.info("Rating : " + t._1 + "  |  Count : " + t._2.spliterator().getExactSizeIfKnown());});
    }

    /**
     * RDD implementation (
     *
     */
    @LogMyTime
    void rddImpl2(SparkSession spark) {

        JavaRDD<Float> typedRdd = spark.table(RATINGS.toString())
                .selectExpr("rating").as(Encoders.FLOAT())
                .toJavaRDD();

        JavaPairRDD<Float, Integer> pairRdd = typedRdd.mapToPair(r -> new Tuple2(r, 1));

        pairRdd.reduceByKey((v1, v2) -> v1 + v2).collect()
                .forEach(tuple -> {
                    LOG.info("Rating : " + tuple._1 + "  |  Count : " + tuple._2);
                });
    }









}
