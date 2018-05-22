/**
 *   Package goal:
 *   <ul>
 *      <li>Provide a simple way to configure and initialize {@link org.apache.spark.sql.SparkSession Spark sessions} in
 *      order to use and <b>share them between different JUnit tests</b></li>
 *      <li>
 *
 *      </li>
 *   </ul>
 *   Main Use Case:
 *   <p>
 *       Running several JUnit tests that require a {@link org.apache.spark.sql.SparkSession} on an isolated
 *       isolated local dev environment, all the JUnit test sharing the same Spark session instance.
 *   </p>
 *   Motivations:
 *   <ul>
 *       <li>Spark session initialisation is rather slow (10 to 20+...  seconds) and as such, is not efficient for unit testing</li>
 *       <li>Running different unit tests - possibly in parallel - that would each require their own Spark session can lead to
 *       initialize different Spark sessions on the local dev environment: it is not only slow, but it is also
 *       tedious to configure properly so that all resources can run in parallel in an isolated environment and avoid
 *       resource conflicts like https://community.cloudera.com/t5/Advanced-Analytics-Apache-Spark/Another-instance-of-Derby-may-have-already-booted-the-database/td-p/56712</li>
 *       <li></li>
 *   </ul>
 *
 *   Sources: strongly inspired by / forked & arranged from:
 *   <ul>
 *       <li>https://beastie.lesfurets.com/articles/apache-spark-unit-testing-junit-java</li>
 *       <li>https://github.com/charithe/kafka-junit/tree/master/src/main/java/com/github/charithe/kafka</li>
 *   </ul>
 *
 *   <b>Limitations:</b>
 *   In its current state, <b>only one Spark Session is initialized per JVM</b> (which is intended and reduce Junit
 *   tests  execution time a lot) <b><s>no matter of which {@link philgbr.exploration.spark.utils.junit5.SparkTestConfig}
 *   configurations</s></b> are requested by the different test classes.
 *
 */
package philgbr.exploration.spark.utils.junit5;
