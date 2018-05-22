package philgbr.exploration.spark.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MovieLensTablesTest {


    /** Checks that every table schema is defined */
    @Test
    public void verifySchemas() {

        for( MovieLensTables m:MovieLensTables.values()){
            assertNotNull(m.schema());
        }
    }
}
