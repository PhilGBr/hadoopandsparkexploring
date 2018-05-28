#!/bin/bash

#  Load environment variables
source 00_env.sh

# Create destination directories
hadoop fs -mkdir -p ${HDFS_MOVIELENS_DATADIR}
hadoop fs -mkdir -p ${HDFS_MOVIELENS_DATADIR_FOR_HIVE}


