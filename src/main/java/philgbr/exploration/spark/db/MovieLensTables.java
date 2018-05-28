package philgbr.exploration.spark.db;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 * Lists tables of the Movielens Database and offers convenient related methods like {@link #schema()} and {@link #getQualifiedName(MovieLensTables, String)}.
 * <p>
 * Note: the <code>users</code> table was not part of the original dataset and was added for the sake our exploration.
 *
 *@see <a href="http://files.grouplens.org/datasets/movielens/ml-20m-README.html">The Movielens Database</a>
 */
public enum MovieLensTables {

	MOVIES("movies"), 
	RATINGS("ratings"), 
	LINKS("links"), 
	TAGS("tags"), 
	GENOME_SCORES("genome_scores"), 
	GENOME_TAGS("genome_tags"),
	USERS("users");

	private String tableName;

	private static Map<MovieLensTables, StructType> schemas;
	static {
		schemas = new HashMap<>();

		schemas.put(MOVIES,
				new StructType(
						new StructField[] { new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("title", DataTypes.StringType, false, Metadata.empty()),
								new StructField("genres", DataTypes.StringType, false, Metadata.empty()) }));
		schemas.put(RATINGS,
				new StructType(
						new StructField[] { new StructField("user_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("rating", DataTypes.FloatType, false, Metadata.empty()),
								new StructField("time", DataTypes.StringType, false, Metadata.empty()) }));

		schemas.put(LINKS,
				new StructType(
						new StructField[] { new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("imdb_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("tmdb_id", DataTypes.IntegerType, false, Metadata.empty()) }));
		schemas.put(TAGS,
				new StructType(
						new StructField[] { new StructField("user_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("tag", DataTypes.StringType, false, Metadata.empty()),
								new StructField("time", DataTypes.StringType, false, Metadata.empty()) }));
		schemas.put(GENOME_SCORES,
				new StructType(
						new StructField[] { new StructField("movie_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("tag_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("relevance", DataTypes.FloatType, false, Metadata.empty()) }));
		schemas.put(GENOME_TAGS,
				new StructType(
						new StructField[] { new StructField("tag_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("tag", DataTypes.StringType, false, Metadata.empty()) }));
		
		schemas.put(USERS,
				new StructType(
						new StructField[] { new StructField("user_id", DataTypes.IntegerType, false, Metadata.empty()),
								new StructField("segment_rater", DataTypes.StringType, false, Metadata.empty()),
								new StructField("segment_tagger", DataTypes.StringType, false, Metadata.empty()) }));
	}
	


	MovieLensTables(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	/**
	 * Returns the current table schema as a  @link org.apache.spark.sql.types.StructType} object
	 */
	public StructType schema() {
		return schemas.get(this);
	}

	/**
	 * Constructs a fully qualified table name for an instance of {@link MovieLensTables#tableName} following the pattern
	 * <b>{@literal <dbSchemaName>.<tableName>}</b>.
	 * 
	 */
	public static String getQualifiedName(MovieLensTables table, @Nullable String dbSchemaName) {
		if (StringUtils.isNotBlank(dbSchemaName)) {
			return String.format("%s.%s", dbSchemaName, table.tableName);
		} else {
			return table.tableName;
		}
	};

}
