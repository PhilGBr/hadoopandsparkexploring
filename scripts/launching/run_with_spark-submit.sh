#!/bin/sh

export SPARK_MAJOR_VERSION=2

MASTER_URL="///spark:127.0.0.1:7077"
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`

spark-submit hadoop-and-spark-exploring-jar-with-dependencies.jar movielens --philgbr.exploration.spark.Main --master $MASTER_URL  --deploy-mode client  2> error-$TIMESTAMP.log | tee output-$TIMESTAMP.log