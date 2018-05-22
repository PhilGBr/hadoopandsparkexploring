#!/bin/bash

#  Load environment variables
source 00_env.sh

beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/genome-scores.csv' OVERWRITE INTO TABLE movielens.genome_scores" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/genome-tags.csv' OVERWRITE INTO TABLE movielens.genome_tags" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/links.csv' OVERWRITE INTO TABLE movielens.links" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/movies.csv' OVERWRITE INTO TABLE movielens.movies" --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/ratings.csv' OVERWRITE INTO TABLE movielens.ratings " --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;
beeline  -u ${HIVE2_SRV_URL} -e "LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/tags.csv' OVERWRITE INTO TABLE movielens.tags " --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;



