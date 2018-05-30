package philgbr.exploration.spark;

import java.lang.reflect.Method;

import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import philgbr.exploration.spark.tasks.FindMoviesWithLowestAvgRating;
import philgbr.exploration.spark.tasks.GroupMoviesByRatings;


/**
 * Launches all the Spark jobs available in this project.<p>
 * 
 * <b>Usage example</b><p>
 * <code>spark-submit hadoop-and-spark-exploring-jar-with-dependencies.jar --philgbr.exploration.spark.Main --master $MASTER_URL
 *   --deploy-mode client</code><p>
 *   
 * <b>Launching configuration</b><p>
 * 
 * SparkSession is initialized using Spark Dynamic Loading: it means that configuration is not specified programmatically,
 * but is given by the runtime environment.<p>
 * 
 * Typically, the <b>master URL</b> will be supplied either by the spark-submit  
 * <code>--master</code> argument, or by the <code>spark.master</code> property set in the default 
 * <code>$SPARK_HOME/conf/spark-defaults.conf</code> file or any alternative properties file configured using 
 * the spark-submit <code>--properties-file</code> argument.
 * 
 * 
 * 
 * @see <a href="https://spark.apache.org/docs/latest/configuration.html#dynamically-loading-spark-properties">Dynamically Loading Spark Properties</a>
 */
public class Main {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        try(SparkSession spark = initSparkSession()) {

        	benchClass(GroupMoviesByRatings.class, GroupMoviesByRatings.METHODS_UNDER_TEST
        			, new Class[] { SparkSession.class, String.class }
        			, spark, "movielens");
        	
        	benchClass(FindMoviesWithLowestAvgRating.class, FindMoviesWithLowestAvgRating.METHODS_UNDER_TEST
        			, new Class[] { SparkSession.class, String.class, int.class, int.class }
        			, spark, "movielens", 1000, 10);

        } catch(Exception e) {
        	LOGGER.error(e.getMessage(), e);
        }
    }
    
    /** 
     * Create a Spark session relying on Spark Properties Dynamic Loading.<p>
     * 
     * 
     * @see <a href="https://spark.apache.org/docs/latest/configuration.html#dynamically-loading-spark-properties">Dynamically Loading Spark Properties</a>
     */ 
    private static SparkSession initSparkSession() {
    	 return SparkSession.builder().appName(Main.class.getName()).enableHiveSupport().getOrCreate();
    }
    
    /**
	 * Sequentially invoke methods (whose names are defined in <code>utMethods</code>) on an instance of class <code>utCLass</code> with a variable number of arguments given by <code>args</code> 
     */
    @SuppressWarnings("rawtypes")
	static void benchClass(Class<?> utClass, String[] utMethods, Class[] signature, Object... args)  throws Exception {
    	String curMethod = null;
		try {
			Object underTest = utClass.newInstance();
			for (String methodName : utMethods) {
				curMethod=methodName;
				Method method = utClass.getMethod(methodName, signature);
				method.invoke(underTest, args);
			}
		} catch(Exception e) {
				LOGGER.error(String.format("An error occured during the execution of %s.%s", utClass.getName(), curMethod), e);
			}
	}    		
}
