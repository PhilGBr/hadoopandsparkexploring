#!/bin/bash

#  Load environment variables
source 00_env.sh


# Counts number of lines of .csv source files as they were downloaded (note: they have 1 header line)
wc -l ${LOCAL_MOVIELENS_DATADIR}/ml-latest/*.csv

# Counts number of rows contained in HIVE tables
beeline  -u ${HIVE2_SRV_URL} -f ./06_count_all.hql --outputformat=csv  --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;