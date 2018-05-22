package philgbr.exploration.spark.tasks;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import philgbr.exploration.spark.utils.LogMyTime;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import static org.apache.spark.sql.functions.*;
import static philgbr.exploration.spark.tasks.MovieLensTables.RATINGS;

public class FindMoviesWithLowestAvgRating {

    private static final Logger LOG = LoggerFactory.getLogger(FindMoviesWithLowestAvgRating.class);

    private static final String QRY_MOVIE_DISTRIBUTION = "select movie_id, avg(rating) as avg_ratings, count(*) as nb_ratings " +
            "from ratings " +
            "group by movie_id " +
            "having nb_ratings > %s " +
            "order by avg_ratings asc, nb_ratings desc " +
            "limit %s";

    /**
     * Dataframe implementation 1.
     */
    @LogMyTime
    void hiveImpl1(SparkSession spark, int minNbRatings, int limit) {
        spark.sql(String.format(QRY_MOVIE_DISTRIBUTION, minNbRatings, limit)).show(true);
    }

    @LogMyTime
    void dfopImpl1(SparkSession spark, int minNbRatings, int limit) {

        Dataset<Row> df = spark.table(RATINGS.toString())
                .select("movie_id", "rating")
                .groupBy(col("movie_id"))
                .agg(count("rating").as("rating_count"), avg("rating").as("avg_rating"))
                .filter("rating_count > " + minNbRatings)
                .orderBy(asc("avg_rating"), desc("rating_count"))
                .limit(limit);
        df.show();


    }

    /**
     * Only valuable to demonstrate how to get "cast" data into strongly used defined type using {@link Encoders}
     */
    @LogMyTime
    void dsImpl1(SparkSession spark, int minNbRatings, int limit) {

        Dataset<Tuple2<Integer, Float>> ds = spark.table(RATINGS.toString())
                //.select(col("movie_id").as(Encoders.INT()), col("rating")  // doesn't work -> need to compose an Encoder for both columns at the same time
                .select("movie_id", "rating").as(Encoders.tuple(Encoders.INT(), Encoders.FLOAT()));

        Dataset<Tuple2<Integer, Float>> groupedDs = ds.groupBy(col("movie_id"))
                .agg(count("rating").as("rating_count"), avg("rating").as("avg_rating")).as(Encoders.tuple(Encoders.INT(), Encoders.FLOAT()))
                .filter("rating_count > " + minNbRatings)
                .orderBy("avg_rating asc", "nb_ratings desc")
                .limit(limit);

        groupedDs.show(true);
    }

    /**
     * RDD implementation (
     */
    @LogMyTime
    void rddImpl1(SparkSession spark, int minNbRatings, int limit) {


        JavaRDD<Tuple2<Integer, Float>> javaRdd = spark.table(RATINGS.toString())
                .select("movie_id", "rating").as(Encoders.tuple(Encoders.INT(), Encoders.FLOAT()))
                .toJavaRDD();

        JavaPairRDD<Integer, Float> pairRdd = javaRdd.mapToPair(t -> {return t;});

        JavaPairRDD<Integer, AvgAccumulator> pairAggRdd =pairRdd.combineByKey(avgZeroValue, avgSeqOp, avgCombOp);


        JavaPairRDD<Integer, AvgAccumulator> filteredPairAggRdd= pairAggRdd.filter( avgAcc -> {return avgAcc._2().count > minNbRatings ? true : false;});

        /**  Those two attempts led to the dreaded o.a.s.SparkException: Task not serializable

            // Attempt #1
            Comparator<Tuple2<Integer,AvgAccumulator>>  orderByComp =
                    comparing(Tuple2::_2, comparing(AvgAccumulator::avg)
                                                    .thenComparing(AvgAccumulator::count).reversed());

            List<Tuple2<Integer,AvgAccumulator>> result = pairAggRdd.takeOrdered(limit,
                    (Comparator<Tuple2<Integer,AvgAccumulator>> & Serializable) orderByComp);

            // Attempt #2
            TupleAvgAccComparator2  orderByComp = new TupleAvgAccComparator2(
                    comparing(Tuple2::_2
                            , comparing(AvgAccumulator::avg)
                            .thenComparing(AvgAccumulator::count).reversed()));

            List<Tuple2<Integer,AvgAccumulator>> result = pairAggRdd.takeOrdered(limit,orderByComp);
        */

       List<Tuple2<Integer,AvgAccumulator>> result = filteredPairAggRdd.takeOrdered(limit,TupleAvgAccComparator.INSTANCE);

        result.forEach(avgAcc -> {LOG.info("movie_id : " + avgAcc._1
                                            + "  |  Avg Rating : " + avgAcc._2().avg()
                                            + "  |  Nb of Rating : " + avgAcc._2().count );});

    }



    public static class AvgAccumulator implements Serializable {

        public float sum;
        public int count;

        public AvgAccumulator(float total, int num) {
            this.sum = total;
            this.count = num;
        }

        public float avg() {
            return sum / count;
        }

        public int count() {
            return count;
        }

        public float sum() {
            return sum;
        }
    }



    Function<Float, AvgAccumulator> avgZeroValue = (n) -> {
            return new AvgAccumulator(n, 1);
    };

    Function2<AvgAccumulator, Float, AvgAccumulator> avgSeqOp = (a, n) -> {
        a.sum += n;
        a.count++;
        return a;
    };

    Function2<AvgAccumulator, AvgAccumulator, AvgAccumulator> avgCombOp = (a, b) -> {
        a.sum += b.sum;
        a.count += b.count;
        return a;
    };

    public static class TupleAvgAccComparator implements Comparator<Tuple2<Integer, AvgAccumulator>>, Serializable {

        final static TupleAvgAccComparator INSTANCE = new TupleAvgAccComparator();

        public int compare(Tuple2<Integer, AvgAccumulator> t1, Tuple2<Integer, AvgAccumulator> t2) {
            AvgAccumulator a1= t1._2();
            AvgAccumulator a2= t1._2();
            int compareAvg = Float.compare(t1._2().avg(), t1._2().avg());  // Ascending order on avg rating
            if(compareAvg != 0) {
                return compareAvg;
            } else {
                return Integer.compare(t2._2().count(), t1._2().count());   // Descending order on # of rating
            }
        }
    }

/*    public static class TupleAvgAccComparator2 implements Comparator<Tuple2<Integer, AvgAccumulator>>, Serializable {

        private Comparator<Tuple2<Integer,AvgAccumulator>> comparator;
        public TupleAvgAccComparator2(Comparator<Tuple2<Integer,AvgAccumulator>> c) {
            comparator=c;
        }

        public int compare(Tuple2<Integer, AvgAccumulator> t1, Tuple2<Integer, AvgAccumulator> t2){
            return comparator.compare(t1, t2);
        }
    }*/

}
