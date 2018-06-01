#!/bin/bash

## Mandatory parameter(s)
##  ->  $1 : schema name (AKA database name) to be created and populated; if not defined
##			 the value 'movielens' is used
##

if [ -n "$1" ]; then
	MY_SCHEMA=$1	
else
	echo "No schema was provided as an argument: using 'movielens' as schema name"
	MY_SCHEMA="movielens"
fi

#  Load environment variables
source 00_env.sh

# Clean up local file system
echo "cleaning up local data directory"
rm -rf ${LOCAL_MOVIELENS_DATADIR}



# Clean up HIVE tables
echo "cleaning up HIVE tables for schema $MY_SCHEMA"

HIVE2_SRV_URL=$(get_hive_cnx_url $MY_SCHEMA)	
beeline -u ${HIVE2_SRV_URL} -f ./99_cleanup_hive_tables.hql --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT
beeline  -u ${HIVE2_SRV_URL} -e "DROP DATABASE IF EXISTS $MY_SCHEMA" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;

# Clean up HDFS
echo "cleaning up HDFS directories"
hadoop fs -rm -r -skipTrash ${HDFS_MOVIELENS_DATADIR}
hadoop fs -rm -r -skipTrash ${HDFS_MOVIELENS_DATADIR_FOR_HIVE}