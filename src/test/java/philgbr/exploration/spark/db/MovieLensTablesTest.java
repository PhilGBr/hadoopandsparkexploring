package philgbr.exploration.spark.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MovieLensTablesTest {

    /** Checks that every table schema is defined */
    @Test
    void verifySchemas() {

        for( MovieLensTables m:MovieLensTables.values()){
            assertNotNull(m.schema());
        }
    }
    
    @Test 
    void testGetQualifiedName() {
    	String aDbSchema="mySchema";
    	assertEquals(String.format("%s.%s", aDbSchema, MovieLensTables.GENOME_SCORES.getTableName())
    					, MovieLensTables.getQualifiedName(MovieLensTables.GENOME_SCORES, aDbSchema));
    	aDbSchema=null;
    	assertEquals(MovieLensTables.GENOME_SCORES.getTableName()
    					, MovieLensTables.getQualifiedName(MovieLensTables.GENOME_SCORES, aDbSchema));
    }
}
