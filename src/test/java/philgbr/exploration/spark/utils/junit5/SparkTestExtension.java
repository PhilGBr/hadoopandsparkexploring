package philgbr.exploration.spark.utils.junit5;

import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.stream;


@SparkTestConfig
class SparkTestExtension implements TestInstancePostProcessor, ParameterResolver {

    private static final Logger LOG = LoggerFactory.getLogger(SparkTestExtension.class);

    private static final SparkTestConfig DEFAULT_CONFIG =
            SparkTestExtension.class.getAnnotation(SparkTestConfig.class);

    private static SparkSession spark;

    private static synchronized SparkSession getSparkOrInitialize(ExtensionContext extensionContext) {
    	
        if (spark == null) { // Note: only one Spark session will be created per JVM, no matter if the requested configurations differ
            SparkTestConfig config = getSparkConfig(extensionContext);

            LOG.info("Starting SparkSession " + config.master() + " for application " + config.appName());
            SparkSession.Builder builder = SparkSession.builder()
                    .appName(config.appName())
                    .master(config.master());
            if(config.enableHiveSupport()){
                builder.enableHiveSupport();
            }
            if(config.options().length>0){
                String[] options = config.options();
                if(options.length%2>0) {
                    throw new ParameterResolutionException("Invalid config options configured for this SparkSession: each option should be constituted a key/value pair");
                }
                for(int i=0; i < options.length; i=i+2) {
                    LOG.info("Spark session option: " + options[i] + " =  " + options[i+1]);
                    builder.config(options[i], options[i+1]);
                }
            }
            spark=builder.getOrCreate();
        }
        return spark;
    }

    private static SparkTestConfig getSparkConfig(ExtensionContext extensionContext) {
        return extensionContext.getElement().map(annotatedElement -> {
            if (annotatedElement.isAnnotationPresent(SparkTestConfig.class)) {
                return annotatedElement.getAnnotation(SparkTestConfig.class);
            } else {
                return DEFAULT_CONFIG;
            }
        }).orElse(DEFAULT_CONFIG);
    }
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.getType() == SparkSession.class)
                .filter(field -> isPublic(field.getModifiers()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Need one public SparkSession field"))
                .set(testInstance, getSparkOrInitialize(context));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(SparkSession.class);

    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return getSparkOrInitialize(extensionContext);
    }

}
