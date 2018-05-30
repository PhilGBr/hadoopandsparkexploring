#!/bin/bash

## Mandatory parameter(s)
##	-> $1: the schema name (AKA database name) in which tables are located

if [ -z "$1" ]; then
	echo "Arg \$1 (the schema name) is missing. Exit script ${0}"
	exit 0
fi

#  Load environment variables
source 00_env.sh

# Clean up local file system
echo "cleaning up local data directory"
rm -rf ${LOCAL_MOVIELENS_DATADIR}



# Clean up HIVE tables
echo "cleaning up HIVE tables"

HIVE2_SRV_URL=$(get_hive_cnx_url $1)	
beeline -u ${HIVE2_SRV_URL} -f ./99_cleanup_hive_tables.hql --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT
beeline  -u ${HIVE2_SRV_URL} -e "DROP DATABASE IF EXISTS $1" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;

# Clean up HDFS
echo "cleaning up HDFS directories"
hadoop fs -rm -r -skipTrash ${HDFS_MOVIELENS_DATADIR}
hadoop fs -rm -r -skipTrash ${HDFS_MOVIELENS_DATADIR_FOR_HIVE}