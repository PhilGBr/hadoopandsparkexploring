package philgbr.exploration.spark.tasks;

import static philgbr.exploration.spark.db.MovieLensTables.RATINGS;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import philgbr.exploration.spark.db.MovielensDbFixture;
import philgbr.exploration.spark.utils.junit5.SparkTest;

@SparkTest
public class FindMoviesWithLowestAvgRatingTest {

    public SparkSession spark; // will be populated by @SparkTest, must be public

    private static final int RESULTSET_LIMIT = 1;
    private static final int NB_MIN_RATINGS = 2;
    
    @BeforeEach
    public void Setup()throws Exception {
        MovielensDbFixture.loadTable(spark, RATINGS, null);
    }
    
    /**  Dynamic invocation of all "under test methods" declared as such in the class under test
     * 
     * @todo avoid relying on a static declaration of "under test methods"; use reflection to list all public virtual methods matching a given signature
     * 
     * @see <a href="https://stackoverflow.com/questions/20524215/comparing-method-parameter-types-filtering-methods-of-a-class-that-are-of-a-ce">... filtering method of a class ...</a>
     */
    @Test
	public void testAllMethod() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {

    	FindMoviesWithLowestAvgRating finder = new FindMoviesWithLowestAvgRating();
		for (String methodName : FindMoviesWithLowestAvgRating.METHODS_UNDER_TEST) {
			Method method = FindMoviesWithLowestAvgRating.class.getMethod(methodName, new Class[] { SparkSession.class, String.class, int.class, int.class });
			method.invoke(finder, spark, null, NB_MIN_RATINGS, RESULTSET_LIMIT);
		}
	}
    

}
