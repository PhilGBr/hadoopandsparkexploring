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
public class GroupMoviesByRatingsTest {

    public SparkSession spark; // will be populated by @SparkTest, must be public

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

    	GroupMoviesByRatings finder = new GroupMoviesByRatings();

		for (String methodName : GroupMoviesByRatings.METHODS_UNDER_TEST) {
			Method method = GroupMoviesByRatings.class.getMethod(methodName, new Class[] { SparkSession.class, String.class});
			method.invoke(finder, spark, null);
		}
	}
}
