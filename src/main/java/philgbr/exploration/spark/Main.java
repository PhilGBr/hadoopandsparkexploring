package philgbr.exploration.spark;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import philgbr.exploration.spark.tasks.FindMoviesWithLowestAvgRating;
import philgbr.exploration.spark.tasks.GroupMoviesByRatings;

public class Main {
    
    private static final String APP_SPARK_CONFIG_FILENAME="spark-session.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

    	//Set<String> sArgs=Arrays.stream(args).collect(Collectors.toSet());
    	
        Properties sparkProperties = load(APP_SPARK_CONFIG_FILENAME);
        SparkConf sparkConfig = initSparkConfig(true,sparkProperties);

        try(SparkSession spark = initSparkSession(sparkConfig, true)) {

        	benchFindMoviesWithLowestAvgRating(spark, "movielens", 1000, 10);
        	benchGroupMoviesByRatings(spark, "movielens");
        }

    }
    
    
    private static void benchGroupMoviesByRatings(SparkSession spark, String dbSchemaName)  {

		GroupMoviesByRatings finderG = new GroupMoviesByRatings();

		for (String methodName : GroupMoviesByRatings.METHODS_UNDER_TEST) {
			try {
				Method method = GroupMoviesByRatings.class.getMethod(methodName, new Class[] { SparkSession.class, String.class });
				method.invoke(finderG, spark, dbSchemaName);
			} catch(Exception e) {
				LOGGER.error(String.format("An error occured during the execution of %s.%s", GroupMoviesByRatings.class.getName(), methodName), e);
			}
		}
	}

    
    private static void benchFindMoviesWithLowestAvgRating(SparkSession spark, String dbSchemaName, int nbMinRatings, int limit)  {

    	FindMoviesWithLowestAvgRating finderF = new FindMoviesWithLowestAvgRating();

		for (String methodName : FindMoviesWithLowestAvgRating.METHODS_UNDER_TEST) {
			try {
				Method method = FindMoviesWithLowestAvgRating.class.getMethod(methodName, new Class[] {  SparkSession.class, String.class, int.class, int.class  });
				method.invoke(finderF, spark, dbSchemaName, nbMinRatings, limit);
			} catch(Exception e) {
				LOGGER.error(String.format("An error occured during the execution of %s.%s", FindMoviesWithLowestAvgRating.class.getName(), methodName), e);
			}
		}
	}
    
    /** If available, loads a properties file and returns its content as a {@link java.util.Properties}.<p>
     */
    public static Properties load(String filename) throws IOException {

        try (InputStream in = Main.class.getClassLoader().getResourceAsStream(filename)) {
            Properties prop = new Properties();
            prop.load(in);
            return prop;
        } catch(NullPointerException npe) {
            // Supplying a properties file is not mandatory
            LOGGER.warn(String.format("No valid config file (%s) was found", filename));
            return null;
        }
    }
    


    /**
     * Load Spark default system properties AND/OR specific properties contained in the <code>config</code> parameter.<p>
     */
    public static SparkConf initSparkConfig(boolean loadDefault, Properties config) {
        SparkConf sc = new SparkConf(loadDefault);
        if(config != null) {
            config.forEach((k,v)-> sc.set((String) k, (String) v));
        }
        LOGGER.info(sc.toDebugString());
        return sc;
    }


    public static SparkSession initSparkSession(SparkConf sc, boolean enableHive) {
        SparkSession.Builder builder = SparkSession.builder().config(sc);
        if(enableHive) {
            builder.enableHiveSupport();
        }
        SparkSession spark= builder.getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");
        return spark;

    }

}
