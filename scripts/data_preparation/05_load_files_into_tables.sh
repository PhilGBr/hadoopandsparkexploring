#!/bin/bash

## Mandatory parameter(s)
##  ->  $1 : schema name (AKA database name) to connect with to the Hive server
##

if [ -z "$1" ]; then
	echo "Arg \$1 (the schema name) is missing. Exit script ${0}"
	exit 0
fi

#  Load environment variables
source 00_env.sh

if [ -z "$2" ]; then
        HIVE2_SRV_URL=$(get_hive_cnx_url)
else
        HIVE2_SRV_URL=$(get_hive_cnx_url $1)
fi

beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/genome-scores.csv' OVERWRITE INTO TABLE $1.genome_scores" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/genome-tags.csv' OVERWRITE INTO TABLE $1.genome_tags" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/links.csv' OVERWRITE INTO TABLE $1.links" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/movies.csv' OVERWRITE INTO TABLE $1.movies" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/ratings.csv' OVERWRITE INTO TABLE $1.ratings " --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/tags.csv' OVERWRITE INTO TABLE $1.tags " --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;



