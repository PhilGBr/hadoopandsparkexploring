package philgbr.exploration.spark.utils.junit5;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Add annotation on Spark test class with public field of type SparkSession, or on Spark test method with parameter
 * of type SparkSession, to get injected SparkSession. The SparkSession is automatically initialized at the beginning
 * of tests and is shared between test instances.
 *
 * <b>Sources</b><ul>
 *     <li>Forked from  https://github.com/lesfurets/lesfurets-conferences/blob/gh-pages/src/apache-spark/src/test/java/com/lesfurets/spark/junit5/extension/SparkTest.java</li>
 *     <li> Modified to allow the Spark session creation be configured using an additional {@link SparkTestConfig} annotation.</li>
 * </ul>
 *
 * <b>Usage on class:</b>
 *
 * <pre><code>

 * {@literal @}SparkTestConfig(master="local", enableHiveSupport = true)
 * {@literal @}SparkTest
 * public class MySparkUnitTest {
 *     // The field "spark" will be injected
 *     public SparkSession spark;
 * }</code></pre>
 *
 * <b>Usage on method:</b>
 *
 * <pre><code>
 * public class MySparkUnitTest {
 *    {@literal @}SparkTestConfig(master="local", enableHiveSupport = true)
 *    {@literal @}Test
 *     // The parameter "spark" will be injected
 *     public void test(SparkSession spark) { }
 * }</code></pre>
 */
@Target({TYPE, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SparkTestExtension.class)
public @interface SparkTest {

}
