package philgbr.exploration.spark.tasks;

import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MovielensDbFixture {

    private static final String FIXTURE_FILES_DIRECTORY = "src/test/resources/ml-lib/";

    // the %s placeholder is to be replaces by the table name (in lowercase)
    private static final String FIXTURE_FILE_PATTERN = FIXTURE_FILES_DIRECTORY+"sample-%s.csv";

    private static  String getFixtureFileName(String tableName) {
        return String.format(FIXTURE_FILE_PATTERN, tableName.toLowerCase());
    }

    // DataFrame Reader Options
    private static Map<String, String> DF_READER_OPTIONS;
    static {
        DF_READER_OPTIONS = new HashMap<>();
        DF_READER_OPTIONS.put("encoding", "UTF-8");
        DF_READER_OPTIONS.put("sep", ",");
        DF_READER_OPTIONS.put("header", "true");
        DF_READER_OPTIONS.put("inferSchema", "false");   //instead, we use an explicit pre-defined schema
    }


    /**
     * Load fixture content for a given {@link MovieLensTables}.
     * @param hiveDatabase optional, if not specified, the content will be loaded in the "default" Hive database
     * @throws Exception when fixture content is not available
     */
    public static void loadTable(SparkSession spark, MovieLensTables table, String hiveDatabase) throws Exception {
        checkFixtureFile(table);
        spark.read()
                .options(DF_READER_OPTIONS)
                .schema(table.schema())
                .csv(getFixtureFileName(table.name()))
                .toDF()
                .createOrReplaceTempView((hiveDatabase != null ? hiveDatabase+"." : "") + table.name());
    }


    /**
     * Load fixture content for all {@link MovieLensTables}.
     * @param hiveDatabase optional, if not specified, the content will be loaded in the "default" Hive database
     * @throws Exception when fixture content is not available for at least one table
     */
    public static void loadDatabase(SparkSession spark, String hiveDatabase) throws Exception {
        checkFixtureFiles();
        for(MovieLensTables t: MovieLensTables.values()) {
            loadTable(spark, t, hiveDatabase);
        }
    }


    /** Checks that every {@link MovieLensTables} table has its own fixture readable file defined */
    private static void checkFixtureFiles() throws Exception{
        for(MovieLensTables t:MovieLensTables.values()) {
            checkFixtureFile(t);
        }
    }
    private static void checkFixtureFile(MovieLensTables table) throws Exception{
        File f= new File(getFixtureFileName(table.name()));
        if(!f.exists() || !f.isFile() || !f.canRead()) {
            throw new Exception("Readable fixture file " + f.getName() + " is missing for table " + table.name());
        }
    }

}
