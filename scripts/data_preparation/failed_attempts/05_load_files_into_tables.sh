#!/bin/bash -xv
#
#       Failed attempts to use variable substitution within .hql scripts
#

#  Load environment variables
source 00_env.sh

# "env" method
# beeline -u ${HIVE2_SRV_URL} -f ./05_load_files_into_tables2.hql

# "hivevar" method
# beeline -u ${HIVE2_SRV_URL} -f ./05_load_files_into_tables.hql -hivevar HDFS_MOVIELENS_DATADIR_FOR_HIVE=$HDFS_MOVIELENS_DATADIR_FOR_HIVE --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT

# "hiveconf" method
# beeline -u ${HIVE2_SRV_URL} -f ./05_load_files_into_tables3.hql -hiveconf HDFS_MOVIELENS_DATADIR_FOR_HIVE=$HDFS_MOVIELENS_DATADIR_FOR_HIVE --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT



