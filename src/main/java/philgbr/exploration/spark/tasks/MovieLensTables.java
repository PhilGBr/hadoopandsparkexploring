package philgbr.exploration.spark.tasks;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.HashMap;
import java.util.Map;

public enum MovieLensTables {

    MOVIES ("movies"),
    RATINGS ("ratings"),
    LINKS ("links"),
    TAGS ("tags"),
    GENOMES_SCORES ("genome_scores"),
    GENOMES_TAGS ("genome_tags");

    private String name;

    private static Map<MovieLensTables, StructType> schemas;
    static {
       schemas = new HashMap<>();

       schemas.put(MOVIES, new StructType( new StructField[] {
               new StructField("movie_id", DataTypes.IntegerType,  false, Metadata.empty()),
               new StructField("title",    DataTypes.StringType,   false, Metadata.empty()),
               new StructField("genres",   DataTypes.StringType,   false, Metadata.empty())
                }));
        schemas.put(RATINGS, new StructType( new StructField[] {
                new StructField("user_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("rating", DataTypes.FloatType, false, Metadata.empty()),
                new StructField("timestamp", DataTypes.StringType, false, Metadata.empty())
        }));

        schemas.put(LINKS, new StructType( new StructField[] {
                new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("imdb_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("tmdb_id", DataTypes.IntegerType, false, Metadata.empty())
        }));
        schemas.put(TAGS, new StructType( new StructField[] {
                new StructField("user_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("tag", DataTypes.StringType, false, Metadata.empty()),
                new StructField("timestamp", DataTypes.StringType, false, Metadata.empty())
        }));
        schemas.put(GENOMES_SCORES, new StructType( new StructField[] {
                new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("tag_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("relevance", DataTypes.FloatType, false, Metadata.empty())
        }));
        schemas.put(GENOMES_TAGS, new StructType( new StructField[] {
                new StructField("tag_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("tag", DataTypes.StringType, false, Metadata.empty())
        }));
    }


    MovieLensTables(String name) {
        this.name=name;
    }

    public String toString() {
        return name;
    }

    /** Returns the current table schema as a {@link org.apache.spark.sql.types.StructType} object */
    public StructType schema() {
        return schemas.get(this);
    }
}
