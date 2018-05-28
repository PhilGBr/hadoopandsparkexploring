#!/bin/bash

#  Load environment variables
source 00_env.sh

# Clean up local file system
echo "cleaning up local data directory"
rm -rf ${LOCAL_MOVIELENS_DATADIR}

# Clean up HIVE tables
echo "cleaning up HIVE tables"
beeline -u ${HIVE2_SRV_URL} -f ./99_cleanup_hive_tables.hql --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT

# Clean up HDFS
echo "cleaning up HDFS directories"
hadoop fs -rm -r -skipTrash ${HDFS_MOVIELENS_DATADIR}
hadoop fs -rm -r -skipTrash ${HDFS_MOVIELENS_DATADIR_FOR_HIVE}