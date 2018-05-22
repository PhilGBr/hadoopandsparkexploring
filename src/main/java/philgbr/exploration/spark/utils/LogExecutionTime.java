package philgbr.exploration.spark.utils;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogExecutionTime {
    private static final String LOG_MESSAGE_FORMAT = "%s.%s execution time: %dms";
    private static final Logger LOG = LoggerFactory.getLogger(LogExecutionTime.class);

    //@Pointcut("execution(@philgbr.exploration.spark.utils.LogMyTime * *(..))")
    @Pointcut("execution(@philgbr.exploration.spark * *(..))")

    public void isAnnotated() {}

    @Around("isAnnotated()")
    public Object get(final ProceedingJoinPoint joinPoint) throws Throwable  {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object retVal = joinPoint.proceed(joinPoint.getArgs());
        stopWatch.stop();
        logExecutionTime(joinPoint, stopWatch);
        return retVal;
    }
    private void logExecutionTime(ProceedingJoinPoint joinPoint, StopWatch stopWatch) {
        String logMessage = String.format(LOG_MESSAGE_FORMAT, joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), stopWatch.getTime());
        LOG.info(logMessage);
    }
}
