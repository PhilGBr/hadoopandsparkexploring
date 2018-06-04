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
import static philgbr.exploration.spark.db.MovieLensTables.*;


public class GroupMoviesByRatings {

	public static final String[] METHODS_UNDER_TEST = {"hiveImpl1"
														, "dfopImpl1", "dfopImpl2"
														, "dsImpl1", "dsImpl2"
														, "rddImpl1"  // <-- Beware that this implementation does NOT scale  
														, "rddImpl2"};

    private static final Logger LOG = LoggerFactory.getLogger(GroupMoviesByRatings.class);


    
    /**
     *  Dataframe implementation 1.
     *
     */
    @LogMyTime
    public void hiveImpl1(SparkSession spark, String dbSchemaName) {
    	
    	String qry = new StringBuilder(" select rating, count(*) as nb_ratings ")
    								.append(" from ").append(getQualifiedName(RATINGS, dbSchemaName))
    								.append(" group by rating ")
    								.toString();
        spark.sql(qry).show(true);
    }

    @LogMyTime
    public void dfopImpl1(SparkSession spark, String dbSchemaName) {
    	
        spark.table(getQualifiedName(RATINGS, dbSchemaName))
                .select(col("rating"))
                .groupBy(col("rating"))
                .count()
                .show(true);
    }

    /**
     * Same as {@link #dfopImpl1(SparkSession)} except we removed the projection so we can measure the impact of such omission
     */

    @LogMyTime
    public void dfopImpl2(SparkSession spark, String dbSchemaName) {

        spark.table(getQualifiedName(RATINGS, dbSchemaName))
              //.select(col("rating")) // oups !
                .groupBy(col("rating"))
                .count()
                .show(true);
    }

    /**
     * Implementation based on the strongly typed Dataset API.
     */
     @LogMyTime
     public void dsImpl1(SparkSession spark, String dbSchemaName) {

         Dataset<Float> ds = spark.table(getQualifiedName(RATINGS, dbSchemaName))
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
    public void dsImpl2(SparkSession spark, String dbSchemaName) {

        Encoder<Rating> ratingEncoder = Encoders.bean(Rating.class);

        //Dataset<Rating> ds = spark.table(RATINGS.toString()).as(ratingEncoder);
            // the line above does not work as column names (ex.: 'movie_id') and field names (ex.: 'movieId') doest not match

        Dataset<Rating> ds =
                spark.table(getQualifiedName(RATINGS, dbSchemaName))
                .selectExpr("movie_id as movieId", "user_id as userId", "rating", "time as timestamp")
                .as(ratingEncoder);

        Dataset<Float> fds=ds.select(col("rating")).as(Encoders.FLOAT());

        RelationalGroupedDataset groupDs = fds.groupBy(col("rating"));
        Dataset<Row> countDf= groupDs.count();
        countDf.show(true);
    }

    /**
     * RDD implementation that <b>DOES NOT scale</b>. because of the use of {@link JavaRDD#groupBy(...)} 
     * that MUST NOT be applied to a large dataset.<p>
     *
     * Instead, use a method relying on the {@link JavaPairRDD#reduceByKey(...)} family (or equivalent perform a first grouping
     * on workers, before shuffling.<p>
     * 
     * @see #rddImpl2(SparkSession, String)
     */
    @DoesNotScale
    @LogMyTime
    public void rddImpl1(SparkSession spark, String dbSchemaName) {
        Encoder<Rating> ratingEncoder = Encoders.bean(Rating.class);

        JavaRDD<Rating> typedRdd = spark.table(getQualifiedName(RATINGS, dbSchemaName))
                                    .selectExpr("movie_id as movieId", "user_id as userId", "rating", "time as timestamp")
                                    .as(ratingEncoder).toJavaRDD();

        JavaPairRDD<Float, Iterable<Rating>> typedPairRdd = typedRdd.groupBy(Rating::getRating);

        typedPairRdd.foreach((Tuple2<Float, Iterable<Rating>> t) ->
                            {LOG.info("Rating : " + t._1 + "  |  Count : " + t._2.spliterator().getExactSizeIfKnown());});
    }

    /**
     * RDD implementation that scales properly.<p>
     *
     */
    @LogMyTime
    public void rddImpl2(SparkSession spark, String dbSchemaName) {

        JavaRDD<Float> typedRdd = spark.table(getQualifiedName(RATINGS, dbSchemaName))
                .selectExpr("rating").as(Encoders.FLOAT())
                .toJavaRDD();

        JavaPairRDD<Float, Integer> pairRdd = typedRdd.mapToPair(r -> new Tuple2<Float, Integer>(r, 1));

        pairRdd.reduceByKey((v1, v2) -> v1 + v2).collect()
                .forEach(tuple -> {
                    LOG.info("Rating : " + tuple._1 + "  |  Count : " + tuple._2);
                });
    }
}
