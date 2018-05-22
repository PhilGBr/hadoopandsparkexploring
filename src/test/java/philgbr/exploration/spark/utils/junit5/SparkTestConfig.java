package philgbr.exploration.spark.utils.junit5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SparkTestConfig {

    /** Define {@link org.apache.spark.sql.SparkSession.Builder#master(String) Spark lauching mode and URL} */
    String master() default "local[*]";

    /** Define {@link org.apache.spark.sql.SparkSession.Builder#appName(String) Spark application name} */
    String appName() default "";

    /** Define if {@link org.apache.spark.sql.SparkSession.Builder#enableHiveSupport()} should be set */
    boolean enableHiveSupport() default true;

    /** Define other {@link org.apache.spark.sql.SparkSession.Builder#config(org.apache.spark.SparkConf) SparkSession options.<p>
     *
     * Note: using a String[] type for this member is a workaround to Java Annotation Type Elements limitations<p>
     *
     * @see https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.6.1
     * @see https://stackoverflow.com/questions/1458535/which-types-can-be-used-for-java-annotation-members
     *
     * As a consequence, {@link #options()}.length shoud always be even, as each config option should be represented
     * by a key/value pair.
     */
    String[]  options() default {"spark.ui.enabled", "false"};
}